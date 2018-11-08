package org.bradfordmiller.dot2img.js

import java.io.StringWriter

import javax.script.ScriptEngineManager
import javax.script.Invocable
import resource._

import scala.io.Source

object JsProcessor {

  //private val vizReader = Source.fromFile("src/main/resources/viz.js").bufferedReader()
  private val vizReader = Source.fromInputStream(getClass.getResourceAsStream("/viz.js")).bufferedReader()
  private val engine = new ScriptEngineManager(null).getEngineByName("nashorn")

  private val vizJs =
    """
      var vizFunc = function(dotdata) {
        Viz(dotdata, 'svg');
      };
    """.stripMargin


  engine.eval(vizReader)
  engine.eval(vizJs)

  def invokeJs(dotData: String) = {

    managed(new StringWriter()).acquireAndGet { sw =>

      val context = engine.getContext()
      val invocable = engine.asInstanceOf[Invocable]

      context.setWriter(sw)
      context.setErrorWriter(sw)

      invocable.invokeFunction("vizFunc", dotData)

      sw.toString()
    }
  }
}
