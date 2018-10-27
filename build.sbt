import NativePackagerHelper._
import ReleaseTransformations._

enablePlugins(JavaAppPackaging)

lazy val commonSettings = Seq(
  organization := "org.bradfordmiller",
  name := "dot2img",
  scalaVersion :="2.12.7"
)

scriptClasspath := Seq("../conf", "*")

mappings in Universal ++= {
  directory("config_files")++
    // copy configuration files to config directory
    contentOf("src/main/resources").toMap.mapValues("conf/" + _)
}

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

//resolvers += "jitpack" at "https://jitpack.io"
publishMavenStyle := false

publishTo := Some(
  Resolver.file("file", new File(Path.userHome.absolutePath + "/.ivy2/local/"))(Resolver.ivyStylePatterns)
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "com.jsuereth" %% "scala-arm" % "2.0",
      "org.apache.xmlgraphics" % "batik-transcoder" % "1.10",
      "org.apache.xmlgraphics" % "batik-codec" % "1.10",
      "commons-io" % "commons-io" % "2.6"/*,
      "com.github.User" % "Repo" % "Tag"*/
    )
  )

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)