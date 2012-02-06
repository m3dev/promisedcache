name := "promisedcache"

organization := "com.m3.promisedcache"

crossScalaVersions := Seq("2.9.1")

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "couchbase.com"         at "http://files.couchbase.com/maven2/",
  "mpeltonen.github.com"  at "http://mpeltonen.github.com/maven/"
)

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  Seq(
    "org.scala-tools.time"    %% "time"                 % "0.5",
    "com.eed3si9n"            %% "sff4s-api"            % "0.1.1",
    "com.eed3si9n"            %% "sff4s-actors"         % "0.1.1",
    "com.weiglewilczek.slf4s" %% "slf4s"                % "1.0.7",
    "spy"                     %  "spymemcached"         % "2.7.1"    % "provided",
    "redis.clients"           %  "jedis"                % "2.0.0"    % "provided",
    "net.sf.ehcache"          %  "ehcache"              % "1.5.0"    % "provided",
    "junit"                   %  "junit"                % "4.10"     % "test",
    "org.scalatest"           %% "scalatest"            % "1.6.1"    % "test"
  )
}

seq(lsSettings :_*)

seq(scalariformSettings: _*)

