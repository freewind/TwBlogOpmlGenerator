package tw.freewind.opml

import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import scala.collection.JavaConversions._
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.SocketConfig
import org.apache.commons.lang3.StringUtils

case class Feed(url: String)

object FeedFinder {

  val httpClient = createHttpClient
  val ValidContentTypesForRss = List("application/rss+xml", "application/atom+xml")

  def findFeed(blog: Blog): Option[Feed] = {
    println(s"blog: $blog")

    val url = blog.url
    val get = createRequest(url)
    val response = httpClient.execute(get)
    try {
      if (response.getStatusLine.getStatusCode == 200) {
        if (currentIsFeed(response)) Some(Feed(url))
        else findFeedFromSource(response, url)
      } else {
        None
      }
    } finally {
      closeQuietly(response)
    }
  }


  private def createRequest(url: String): HttpGet = {
    val request = new HttpGet(url)
    // some servers will check the request headers, or just response 403
    request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    request.setHeader("Accept-Encoding", "gzip,deflate,sdch")
    request.setHeader("Accept-Language", "en-US,en;q=0.8,nb;q=0.6,zh-CN;q=0.4")
    request.setHeader("Cache-Control", "max-age=0")
    request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36")
    request
  }

  private def currentIsFeed(response: CloseableHttpResponse): Boolean = {
    val contentType = response.getEntity.getContentType.getValue
    val contains: Boolean = contentType.contains("xml")
    contains
  }

  private def findFeedFromSource(response: CloseableHttpResponse, url: String): Option[Feed] = {
    val content = IOUtils.toString(response.getEntity.getContent)
    val document = Jsoup.parse(content)
    document.setBaseUri(url)

    def findFromLinkTags = document.select("link").toList
      .find(tag => ValidContentTypesForRss.contains(tag.attr("type")))
      .map(tag => Feed(tag.attr("abs:href")))

    def findFeedFromAllLinks = document.select("a").toList
      .map(_.attr("abs:href"))
      .find({ href => val path = StringUtils.substringAfterLast(href, "/")
        println("href: " + href)
        List("feed", "atom", "rss").exists(path.contains)
      })
      .map(Feed)

    findFromLinkTags orElse findFeedFromAllLinks
  }

  private def closeQuietly(response: CloseableHttpResponse) {
    try {
      response.close()
    } catch {
      case _: Exception =>
    }
  }

  private def createHttpClient: CloseableHttpClient = {
    val ThreeSeconds = 3000
    val socketConfig = SocketConfig.custom().setSoTimeout(ThreeSeconds).build()
    val requestConfig = RequestConfig.custom().setConnectTimeout(ThreeSeconds).setConnectTimeout(ThreeSeconds).build()
    HttpClientBuilder.create
      .setDefaultRequestConfig(requestConfig)
      .setDefaultSocketConfig(socketConfig).build()
  }
}
