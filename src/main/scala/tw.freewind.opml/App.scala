package tw.freewind.opml

import java.io.File
import scala.xml.Elem
import org.apache.commons.io.FileUtils

object App {

  val BlogSourceFile = new File("blogs.txt")
  val TargetOpmlFile = new File("tw-blogs.opml")

  def main(args: Array[String]) {
    println(s"Reading blogs from file: ${BlogSourceFile.getAbsolutePath}\n")

    val blogs = BlogSourceFileParser(BlogSourceFile)
    val opml = OpmlGenerator(blogs)
    save(opml, TargetOpmlFile)

    println("\nSaved to: " + TargetOpmlFile.getAbsolutePath)
  }

  def save(opml: Elem, targetFile: File) {
    FileUtils.writeStringToFile(targetFile, opml.toString())
  }

}