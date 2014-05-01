name := "tw-blog-opml-generator"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.3.3",
  "commons-io" % "commons-io" % "1.3.2",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.jsoup" % "jsoup" % "1.7.3"
)

