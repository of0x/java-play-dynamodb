name := "java-play-dynamodb"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-hibernate3" % "2.2.3",
  "com.amazonaws" % "aws-java-sdk" % "1.9.6"
)     

play.Project.playJavaSettings
