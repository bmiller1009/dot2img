package org.bradfordmiller.dot2img

import java.io.File

import javax.script.ScriptException
import org.apache.commons.io.FileUtils
import org.scalatest.{BeforeAndAfterAll, FunSuite}

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

  test("Dot2Img should save correct file type") {
    getFiles().foreach {case (createdFile, testFile) =>
      val p = Dot2Img.save(data, createdFile.getPath)
      assert(FileUtils.contentEquals(p.toFile(), testFile))
    }
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
