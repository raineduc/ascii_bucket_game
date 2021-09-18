name := "ascii_bucket_game"

version := "0.1"

scalaVersion := "2.13.6"

val jlineVersion = "3.20.0"

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "5.9.0",
  "org.jline" % "jline" % jlineVersion
)