ThisBuild / scalaVersion := "3.2.0"

lazy val webpage = project
  .in(file("webpage"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.3.0"
    )
  )
  .dependsOn(core.js)

lazy val webserver = project
  .in(file("webserver"))
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.8.0",
      "org.scalameta" %% "munit" % "1.0.0-M3" % Test
    ),
    Compile / resourceGenerators += Def.task {
      val source = (webpage / Compile / scalaJSLinkedFile).value.data
      val dest = (Compile / resourceManaged).value / "assets" / "main.js"
      IO.copy(Seq(source -> dest))
      Seq(dest)
    },
    run / fork := true
  )
  .dependsOn(core.jvm)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "2.0.0",
      "org.scalameta" %%% "munit" % "1.0.0-M3" % Test
    )
  )
