import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play21-frames-orientdb-plugin"
  val appVersion      = "1.1.8-module-2.4.3"
  val orientDBVersion = "1.5.1"
  val tinkerpopVersion = "2.4.0"  

  val appDependencies = Seq(  
    "com.wingnest.play2" % "play21-frames-module_2.10" % "2.4.3",
    "com.orientechnologies" % "orientdb-core" % {orientDBVersion},
 	"com.orientechnologies" % "orient-commons" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-client" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-nativeos" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-server" % {orientDBVersion},    
    "com.orientechnologies" % "orientdb-object" % {orientDBVersion},
    "com.orientechnologies" % "orientdb-enterprise" % {orientDBVersion}	,
    "com.orientechnologies" % "orientdb-distributed" % {orientDBVersion}, 
    
//    "com.tinkerpop.blueprints" % "blueprints-core" % {tinkerpopVersion},
//    "com.tinkerpop.blueprints" % "blueprints-test" % {tinkerpopVersion},
//    "com.tinkerpop" % "frames" % {tinkerpopVersion},
    "com.tinkerpop.blueprints" % "blueprints-orient-graph" % {tinkerpopVersion} excludeAll(
          ExclusionRule(organization = "com.orientechnologies")
      ), 

	"com.hazelcast" % "hazelcast" % "3.0",      
//    "org.javassist" % "javassist" % "3.17.1-GA",
//	"commons-collections" % "commons-collections" % "3.2.1",
    javaCore
  )

    
  val main = play.Project(appName, appVersion, appDependencies).settings(
    publishArtifact in(Compile, packageDoc) := false,
    organization := "com.wingnest.play2",
    resolvers += "Sonatype OSS Snapshot" at "https://oss.sonatype.org/content/repositories/snapshots",    
    resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/public/"
  )

}
