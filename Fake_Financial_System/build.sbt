
name := "akka-quickstart-java"

version := "1.0"

scalaVersion := "2.13.1"

lazy val akkaVersion = "2.6.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "junit" % "junit" % "4.13.1" ,
  "com.novocode" % "junit-interface" % "0.11" ,
  "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.5.2",
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
"com.typesafe.akka" %% "akka-persistence-testkit" % akkaVersion )
