name := "Jobs API Analysis"

version := "0.1"

val versions = new {
    val jackson = "3.0.8"
    val scalaLogging = "3.9.0"
    val logback = "1.2.3"
    val scalaTest = "3.0.8"
}

scalaVersion := "2.13.0"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"