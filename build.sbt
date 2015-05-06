name := "akkaUsage"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-http-core-experimental_2.10" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.10"

libraryDependencies += "com.typesafe.akka" % "akka-stream-experimental_2.10" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-http-scala-experimental_2.10" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json-experimental_2.10" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-http-testkit-scala-experimental_2.10" % "1.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-cluster_2.10" % "2.3.1"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.3.1"
