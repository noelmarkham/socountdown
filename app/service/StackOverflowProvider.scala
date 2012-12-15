package service

import play.api.libs.concurrent.Promise

trait StackOverflowProvider {

  def getScoreForUser(userId: Int): Promise[Int]
}
