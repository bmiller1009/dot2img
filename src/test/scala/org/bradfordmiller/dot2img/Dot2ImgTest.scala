package org.bradfordmiller.dot2img

import java.io.File
import java.nio.file.Paths

import javax.script.ScriptException
import org.apache.commons.io.FileUtils
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.w3c.dom.Attr
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.diff.{Comparison, ComparisonResult, DifferenceEvaluator}

class IgnoreAttributeDifferenceEvaluator(attributeName: String) extends DifferenceEvaluator {
  override def evaluate(comparison: Comparison, outcome: ComparisonResult): ComparisonResult = {
    if (outcome eq ComparisonResult.EQUAL) return outcome
    val controlNode = comparison.getControlDetails.getTarget
    if (controlNode.isInstanceOf[Attr]) {
      val attr = controlNode.asInstanceOf[Attr]
      if (attr.getName.equals(attributeName)) return ComparisonResult.SIMILAR
    }
    outcome
  }
}

class Dot2ImgTest extends FunSuite with BeforeAndAfterAll {

  val path = "src/test/resources/"
  val testPath = s"${path}testfiles/"
  val data = "digraph G {\n  \"Welcome\" -> \"To\"\n  \"To\" -> \"Web\"\n  \"To\" -> \"GraphViz!\"\n}"

  private def getFiles() = {
    List(
      (new File(s"${path}test.svg"), new File(s"${testPath}test.svg")),
      (new File(s"${path}test.png"), new File(s"${testPath}test.png")),
      (new File(s"${path}test.jpeg"), new File(s"${testPath}test.jpeg"))
    )
  }

  private def deleteCreatedFiles() = {
    getFiles().foreach {case (createdFile, _) =>
      if(createdFile.exists())
        createdFile.delete()
    }
  }

  override def beforeAll(): Unit = {
    deleteCreatedFiles()
  }

  test("Dot2Img should create a valid svg xml definition") {
    val p = Dot2Img.save(data, s"${path}test.svg")
    val generatedFileXml = FileUtils.readFileToString(p.toFile())
    val controlFileXml = FileUtils.readFileToByteArray(Paths.get(s"${testPath}test.svg").toFile())

    //Note - the xml attribute id is ignored in the svg because it is dynamically set in viz.js
    val idDiff = DiffBuilder.compare(controlFileXml).withTest(generatedFileXml)
      .withDifferenceEvaluator(new IgnoreAttributeDifferenceEvaluator("id"))
      .checkForSimilar().build()

    assert(!idDiff.hasDifferences())
  }

  test("Dot2Img should fail when given an unknown file extension") {
    assertThrows[NoSuchElementException] {
      Dot2Img.save(data, "src/test/resources/test.dat")
    }
  }

  test("Dot2Img should fail when given an incorrect dot definition") {
    val badData = "Blah"
    assertThrows[ScriptException] {
      Dot2Img.save(badData, "src/test/resources/testformat.jpg")
    }
  }

  override def afterAll(): Unit = {
    deleteCreatedFiles()
  }
}
