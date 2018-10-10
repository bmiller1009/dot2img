package org.bradfordmiller.dot2img

import java.io.{ByteArrayInputStream, FileOutputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.apache.batik.transcoder.{TranscoderInput, TranscoderOutput}
import org.apache.batik.transcoder.image.{JPEGTranscoder, PNGTranscoder}
import org.apache.commons.io.FilenameUtils
import org.bradfordmiller.dot2img.js.JsProcessor
import resource._

object Dot2Img {

  import JsProcessor._

  def save(dotdata: String, path: String) = {

    val fileExt = FilenameUtils.getExtension(path)

    val svg = invokeJs(dotdata)

    fileExt match {
      case "svg" => Files.write(Paths.get(path), svg.getBytes(StandardCharsets.UTF_8))
      case ext: String => {
        val svgInput = new TranscoderInput(new ByteArrayInputStream(svg.getBytes()))
        managed(new FileOutputStream(path)).acquireAndGet { os =>

          val output = new TranscoderOutput(os)

          val transcoder =
            if (ext == "png") new PNGTranscoder()
            else if (ext == "jpeg") {
              val jpegTranscoder = new JPEGTranscoder()
              jpegTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1.toFloat)
              jpegTranscoder
            }
            else throw new NoSuchElementException

          transcoder.transcode(svgInput, output)
          os.flush()
        }
      }
    }
  }
}
