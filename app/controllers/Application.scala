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
    Async {
      provider.getScoreForUser(id).map { score =>

        if (score < threshold) Ok(negativeView.render(score)) else Ok(positiveView.render(score))
      }
    }
  }
}

object Application extends Application(
  StackOverflowWebServiceProvider,
  22656,
  1000000,
  views.html.positive,
  views.html.negative
)