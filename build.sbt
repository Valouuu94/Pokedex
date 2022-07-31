val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Pokedex",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1",
      "com.softwaremill.sttp.client3" %% "core" % "3.7.2",

    )
  )
