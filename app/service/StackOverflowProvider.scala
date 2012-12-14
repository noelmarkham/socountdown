package service

trait StackOverflowProvider {

  def getScoreForUser(userId: Int): Int
}
