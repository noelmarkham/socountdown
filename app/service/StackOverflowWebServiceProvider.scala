package service

import play.api.libs.ws.WS
import play.api.libs.concurrent.Promise
import io.Source
import play.api.libs.json.Json
import java.util.zip.GZIPInputStream

object StackOverflowWebServiceProvider extends StackOverflowProvider {

  val urlTemplate = "https://api.stackexchange.com/2.1/users/%d?order=desc&sort=reputation&site=stackoverflow"

  def getScoreForUser(userId: Int): Promise[Int] = {
    WS.url(urlTemplate.format(userId)).get().map { response =>
      val stream = new GZIPInputStream(response.getAHCResponse.getResponseBodyAsStream)
      val json = Json.parse(Source.fromInputStream(stream).getLines().mkString)

      (json \ "items" \\ "reputation").mkString.toInt
    }
  }
}
