package controllers

import play.api.mvc._
import service.{SoUser, StackOverflowWebServiceProvider, StackOverflowProvider}
import play.api.templates.{Template1, Html}

class Application(provider: StackOverflowProvider,
                  defaultUser: Int,
                  threshold: Int,
                  positiveView: Template1[SoUser, Html],
                  negativeView: Template1[SoUser, Html]) extends Controller {
  
  def index = indexForUser(defaultUser)

  def indexForUser(id: Int) = Action {
    Async {
      provider.getScoreForUser(id).map {
        case SoUser(imageUrl, rep, started) =>
          if (rep < threshold) Ok(negativeView.render(SoUser(imageUrl, rep, started)))
          else Ok(positiveView.render(SoUser(imageUrl, rep, started)))
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