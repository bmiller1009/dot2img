package org.bradfordmiller.dot2img.js

import java.io.StringWriter

import javax.script.ScriptEngineManager
import javax.script.Invocable

import scala.io.Source
import resource._

object JsProcessor {

  private val vizReader = Source.fromResource("viz.js").bufferedReader()
  private val engine = new ScriptEngineManager().getEngineByName("nashorn")

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
