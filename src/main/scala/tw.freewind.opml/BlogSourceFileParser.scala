package tw.freewind.opml

import java.io.File
import scala.io.Source
import org.apache.commons.lang3.StringUtils

case class Blog(name: String, url: String)

object BlogSourceFileParser {

  def apply(file: File): List[Blog] = {
    for {
      line <- Source.fromFile(file).getLines().toList.map(_.trim)
      if !line.isEmpty
      if !line.startsWith("#") // comment line
      blog <- parseLine(line)
    } yield blog
  }

  private def parseLine(line: String): Option[Blog] = {
    line.split("\\s+") match {
      case Array(name, url) => Some(Blog(name, fixUrl(url)))
      case _ =>
        println(s"Invalid format in this line: $line")
        None
    }
  }

  private def fixUrl(url: String) = {
    val fixedUrl = StringUtils.stripEnd(url, "/")
    if (fixedUrl.startsWith("http:") || fixedUrl.startsWith("https:")) {
      fixedUrl
    } else {
      "http://" + fixedUrl
    }
  }

}