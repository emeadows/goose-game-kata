name := "goose-game-kata"

version := "1.0"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
