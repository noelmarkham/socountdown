package service

import play.api.libs.concurrent.Promise
import java.util.Date

trait StackOverflowProvider {

  def getScoreForUser(userId: Int): Promise[SoUser]
}

case class SoUser(name: String,
                  imageUrl: String,
                  soPage: String,
                  rep: Int,
                  started: Date)