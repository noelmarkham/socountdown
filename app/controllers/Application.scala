package controllers

import play.api.mvc._
import service.{StackOverflowWebServiceProvider, StackOverflowProvider}
import play.api.templates.{Template1, Html}

class Application(provider: StackOverflowProvider,
                  defaultUser: Int,
                  threshold: Int,
                  positiveView: Template1[Int, Html],
                  negativeView: Template1[Int, Html]) extends Controller {
  
  def index = indexForUser(defaultUser)

  def indexForUser(id: Int) = Action {
    val score = provider.getScoreForUser(id)
    println("Received score [%d] for user [%d]".format(score, id))
    if(score < threshold) Ok(negativeView.render(score)) else Ok(positiveView.render(score))
  }
}

object Application extends Application(
  StackOverflowWebServiceProvider,
  1,
  1000000,
  views.html.index,
  views.html.index
)