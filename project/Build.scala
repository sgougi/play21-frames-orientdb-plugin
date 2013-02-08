import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21-frames-orientdb-plugin"
  val appVersion      = "1.0-SNAPSHOT"
  val orientDBVersion = "1.3.0"
  val tinkerpopVersion = "2.2.0"  
//  val orientDBVersion = "1.4.0-SNAPSHOT"

  val appDependencies = Seq(  
    "com.wingnest.play2" % "play21-frames-module_2.10" % "1.0-SNAPSHOT",
    
    "com.orientechnologies" % "orientdb-core" % {orientDBVersion},
 	"com.orientechnologies" % "orient-commons" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-client" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-nativeos" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-server" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-object" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-enterprise" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-distributed" % {orientDBVersion},
    
    "com.tinkerpop.blueprints" % "blueprints-core" % {tinkerpopVersion},
    "com.tinkerpop.blueprints" % "blueprints-test" % {tinkerpopVersion},
    "com.tinkerpop" % "frames" % {tinkerpopVersion},
//    "com.tinkerpop.blueprints" % "blueprints-orient-graph" % {tinkerpopVersion}, 
                    
    "com.hazelcast" % "hazelcast" % "2.1.2",
//    "org.javassist" % "javassist" % "3.17.1-GA",
//	"commons-collections" % "commons-collections" % "3.2.1",
    javaCore
  )
    
  val main = play.Project(appName, appVersion, appDependencies).settings(
    organization := "com.wingnest.play2",
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/public/"
  )

}
