package service

import play.api.libs.ws.WS
import play.api.libs.concurrent.Promise
import io.Source
import play.api.libs.json.Json
import java.util.zip.GZIPInputStream
import java.util.Date
import actors.threadpool.TimeUnit

object StackOverflowWebServiceProvider extends StackOverflowProvider {

  val urlTemplate = "https://api.stackexchange.com/2.1/users/%d?order=desc&sort=reputation&site=stackoverflow"
  val QuoteRemovalPattern = "\"(.*)\"".r

  def getScoreForUser(userId: Int): Promise[SoUser] = {
    WS.url(urlTemplate.format(userId)).get().map { response =>
      val stream = new GZIPInputStream(response.getAHCResponse.getResponseBodyAsStream)
      val json = Json.parse(Source.fromInputStream(stream).getLines().mkString)
      println(json)
      val QuoteRemovalPattern(name) = (json \ "items" \\ "display_name").mkString
      val imageUrl = (json \ "items" \\ "profile_image").mkString
      val soPage = (json \ "items" \\ "link").mkString
      val rep = (json \ "items" \\ "reputation").mkString.toInt
      val creationDate = new Date(TimeUnit.SECONDS.toMillis((json \ "items" \\ "creation_date").mkString.toLong))

      SoUser(name, imageUrl, soPage, rep, creationDate)
    }
  }
}
