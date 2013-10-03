import sbt._
import Keys._
import play.Project._


object ApplicationBuild extends Build {

  val appName         = "play21-frames-orientdb-simple-app"
  val appVersion      = "1.1.6-module-2.4.1"
  val orientDBVersion = "1.5.0"
  val tinkerpopVersion = "2.4.0"
    
  val appDependencies = Seq(
      "com.wingnest.play2" % "play21-frames-orientdb-plugin_2.10" % "1.1.6-module-2.4.1" excludeAll(
          ExclusionRule(organization = "com.tinkerpop.blueprints")
      ),

      "com.tinkerpop.blueprints" % "blueprints-orient-graph" % {tinkerpopVersion} excludeAll(
          ExclusionRule(organization = "com.orientechnologies")
      ),
      
//      "com.hazelcast" % "hazelcast" % "3.0",
//      "org.javassist" % "javassist" % "3.17.1-GA", 
//      "org.apache.httpcomponents" % "httpclient" % "4.2.2",
//      "commons-lang" % "commons-lang" % "2.6",
      javaCore
  )
  
  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += "Sonatype OSS Snapshot" at "https://oss.sonatype.org/content/repositories/snapshots",    
    resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/public/"
  )

}
