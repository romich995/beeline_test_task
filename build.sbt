

ThisBuild / scalaVersion     := "2.12.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organizationName := "rbiktayrov"

lazy val root = (project in file("."))
  .settings(
    name := "PopularProdct",
   ThisBuild / assemblyMergeStrategy  := { 
  case PathList("META-INF","services",xs @ _*) => MergeStrategy.filterDistinctLines // Added this 
  case PathList("META-INF",xs @ _*) => MergeStrategy.discard  
  case _ => MergeStrategy.first

},
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.1",
    /*libraryDependencies += scalaTest % Test,*/
    libraryDependencies ++= Seq(
  ("org.apache.spark" %% "spark-core" % "3.1.2").
    exclude("org.apache.spark", "unused").
    exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("commons-logging", "commons-logging").
    exclude("com.esotericsoftware.minlog", "minlog"),
  "org.apache.spark" %% "spark-sql" % "3.1.2",
)
  )


// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
