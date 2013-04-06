import sbt._
import Keys._
import play.Project._


object ApplicationBuild extends Build {

  val appName         = "play21-frames-orientdb-simple-app"
  val appVersion      = "2.2.0-1.0"

  val appDependencies = Seq(
      "com.wingnest.play2" % "play21-frames-orientdb-plugin_2.10" % "2.2.0-1.0",
//      "org.apache.httpcomponents" % "httpclient" % "4.2.2",
//      "commons-lang" % "commons-lang" % "2.6",
      javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
