import sbt._
import Keys._

object PromisedCacheBuild extends Build {

  lazy val promisedCache = Project("promisedcache", file("."), settings = mainSettings)

  lazy val mainSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ Seq(
    sbtPlugin := false,
    organization := "com.m3.promisedcache",
    name := "promisedcache",
    version := "0.1",
    publishTo <<= (version) {
      version: String =>
        Some(
          Resolver.file("GitHub Pages", Path.userHome / "github" / "m3dev.github.com" / "mvn-repo" / {
            if (version.trim.endsWith("SNAPSHOT")) "snapshots" else "releases"
          })
        )
    },
   publishMavenStyle := true,
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

}


