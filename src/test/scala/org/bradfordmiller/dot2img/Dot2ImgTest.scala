package org.bradfordmiller.dot2img

import org.scalatest.FunSuite

class Dot2ImgTest extends FunSuite {

  test("Dot2Img should save correct file type") {

    val data = "digraph G {\n  \"Welcome\" -> \"To\"\n  \"To\" -> \"Web\"\n  \"To\" -> \"GraphViz!\"\n}"

    Dot2Img.save(data, "src/test/resources/test.svg")
    Dot2Img.save(data, "src/test/resources/test.png")
    Dot2Img.save(data, "src/test/resources/test.jpeg")
  }

}
