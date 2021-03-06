import NativePackagerHelper._
import ReleaseTransformations._

enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
  organization := "org.bradfordmiller",
  name := "dot2img",
  scalaVersion :="2.12.7"
)

packageSummary in Linux := "Small library for converting DOT (graph description language) to various image formats"
packageSummary in Windows := "Small library for converting DOT (graph description language) to various image formats"
packageDescription := "Small library for converting DOT (graph description language) to various image formats"

crossScalaVersions := Seq("2.12.7", "2.11.12")

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishTo := sonatypePublishTo.value
publishMavenStyle := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "com.jsuereth" %% "scala-arm" % "2.0",
      "org.apache.xmlgraphics" % "batik-transcoder" % "1.10",
      "org.apache.xmlgraphics" % "batik-codec" % "1.10",
      "commons-io" % "commons-io" % "2.6",
      "org.apache.ws.security" % "wss4j" % "1.6.19",
      "org.xmlunit" % "xmlunit-core" % "2.2.1" % Test,
      "org.xmlunit" % "xmlunit-matchers" % "2.2.1" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    )
  )

releaseProcess := Seq[ReleaseStep](
  inquireVersions,                        // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  publishArtifacts,
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)

pomExtra := (
  <url>https://github.com/bmiller1009/dot2img</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:bmiller1009/dot2img.git/</url>
    <connection>scm:git@github.com:bmiller1009/dot2img.git</connection>
  </scm>
    <developers>
      <developer>
        <id>bmiller1009</id>
        <name>Bradford Miller</name>
        <url>https://github.com/bmiller1009</url>
      </developer>
    </developers>
  )