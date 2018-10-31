package org.bradfordmiller.dot2img

import java.io.{ByteArrayInputStream, FileOutputStream, StringReader}

import java.nio.file.{Files, Paths}

import javax.xml.parsers.DocumentBuilderFactory

import org.apache.batik.transcoder.{TranscoderInput, TranscoderOutput}
import org.apache.batik.transcoder.image.{JPEGTranscoder, PNGTranscoder}

import org.apache.commons.io.FilenameUtils
import org.bradfordmiller.dot2img.js.JsProcessor
import resource._

import org.apache.ws.security.util.XMLUtils
import org.xml.sax.InputSource

object Dot2Img {

  private val acceptedExts = Seq("svg","png","jpeg","jpg")

  private def overwriteWidthHeight(data: String, width: Int, height: Int, unit: String): String = {
    val doc =
      DocumentBuilderFactory.newInstance()
      .newDocumentBuilder()
      .parse(new InputSource(new StringReader(data)))

    val root = doc.getDocumentElement()
    root.setAttribute("width", s"${width}$unit")
    root.setAttribute("height", s"${height}$unit")

    XMLUtils.PrettyDocumentToString(doc)
  }

  def save(dotdata: String, path: String, width: Int = 200, height: Int = 200, unit: String = "pt") = {

    val fileExt = FilenameUtils.getExtension(path).toLowerCase()

    if(!acceptedExts.contains(fileExt)) throw new NoSuchElementException(s"Unsupported file format: $fileExt")

    val rawSVG = JsProcessor.invokeJs(dotdata)

    val svg = overwriteWidthHeight(rawSVG, width, height, unit)

    fileExt match {
      case "svg" => Files.write(Paths.get(path), svg.getBytes())
      case ext: String => {
        val svgInput = new TranscoderInput(new ByteArrayInputStream(svg.getBytes()))
        managed(new FileOutputStream(path)).acquireAndGet { os =>
          val output = new TranscoderOutput(os)
          val transcoder = ext match {
            case "png" => new PNGTranscoder()
            case "jpeg" | "jpg" => {
              val jpegTranscoder = new JPEGTranscoder()
              jpegTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1f)
              jpegTranscoder
            }
          }
          transcoder.transcode(svgInput, output)
          os.flush()
          Paths.get(path)
        }
      }
    }
  }
}
