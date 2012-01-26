resolvers ++= Seq(
  "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.0.0")

resolvers ++= Seq(
  "less is" at "http://repo.lessis.me",
  "coda" at "http://repo.codahale.com")

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.1")

