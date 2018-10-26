import NativePackagerHelper._

enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
  organization := "org.bradfordmiller",
  name := "dot2img",
  version := "0.0.1-SNAPSHOT",
  scalaVersion :="2.12.7",
  scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint")
)

scriptClasspath := Seq("../conf", "*")

mappings in Universal ++= {
  directory("config_files")++
    // copy configuration files to config directory
    contentOf("src/main/resources").toMap.mapValues("conf/" + _)
}

lazy val root = (project in file(".")).
  settings(commonSettings: _*).

  settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.jsuereth" %% "scala-arm" % "2.0",
      "org.apache.xmlgraphics" % "batik-transcoder" % "1.10",
      "org.apache.xmlgraphics" % "batik-codec" % "1.10",
      "commons-io" % "commons-io" % "2.6"
    )
  )