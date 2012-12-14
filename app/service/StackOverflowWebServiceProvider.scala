package service

object StackOverflowWebServiceProvider extends StackOverflowProvider {
  def getScoreForUser(userId: Int): Int = {
    throw new Exception("Not implemented yet")
  }
}
