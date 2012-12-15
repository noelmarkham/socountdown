package service

import play.api.libs.ws.WS
import play.api.libs.concurrent.Promise
import io.Source
import play.api.libs.json.Json
import java.util.zip.GZIPInputStream
import java.util.Date
import actors.threadpool.TimeUnit
import play.Logger

object StackOverflowWebServiceProvider extends StackOverflowProvider {

  val urlTemplate = "https://api.stackexchange.com/2.1/users/%d?order=desc&sort=reputation&site=stackoverflow&key=wGcTdbB*YEIh80zs5gyC*Q(("
  val QuoteRemovalPattern = "\"(.*)\"".r

  def getScoreForUser(userId: Int, threshold: Int): Promise[SoUser] = {
    WS.url(urlTemplate.format(userId)).get().map { response =>
      val stream = new GZIPInputStream(response.getAHCResponse.getResponseBodyAsStream)
      val json = Json.parse(Source.fromInputStream(stream).getLines().mkString)
      Logger.info(json.toString())
      val QuoteRemovalPattern(name) = (json \ "items" \\ "display_name").mkString
      val imageUrl = (json \ "items" \\ "profile_image").mkString
      val soPage = (json \ "items" \\ "link").mkString
      val rep = (json \ "items" \\ "reputation").mkString.toInt
      val creationDate = new Date(TimeUnit.SECONDS.toMillis((json \ "items" \\ "creation_date").mkString.toLong))
      val expectedDate = projectedDate(rep, threshold, creationDate).get.toString

      val quotaRemaining = (json \ "quota_remaining")
      val quotaMax = (json \ "quota_max")

      Logger.info("Stack Overflow API queries quota remaining: [%s] of max [%s]".format(quotaRemaining, quotaMax))

      SoUser(name, imageUrl, soPage, rep, expectedDate)
    }
  }

  def projectedDate(currentRep: Int, threshold: Int, startDate: Date): Option[Date] = {
    if(currentRep > threshold) None
    else {
      val millisTillNow = System.currentTimeMillis() - startDate.getTime
      val newtimestamp = ((millisTillNow / currentRep) * threshold) + startDate.getTime
      Some(new Date(newtimestamp))
    }
  }
}
