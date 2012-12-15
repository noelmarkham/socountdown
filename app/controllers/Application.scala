package controllers

import play.api.mvc._
import service.{SoUser, StackOverflowWebServiceProvider, StackOverflowProvider}
import play.api.templates.{Template1, Html}
import play.api.Play.current
import play.api.cache.Cache
import play.Logger

class Application(provider: StackOverflowProvider,
                  defaultUser: Int,
                  threshold: Int,
                  positiveView: Template1[SoUser, Html],
                  negativeView: Template1[SoUser, Html]) extends Controller {
  
  def index = indexForUser(defaultUser)

  def indexForUser(id: Int) = Action {
    Async {
      val scorePromise = Cache.getOrElse("json", 600){
        Logger.info("Refreshing JSON from cache")
        provider.getScoreForUser(id, threshold)
      }
      scorePromise.map {
        case SoUser(name, imageUrl, soPage, rep, date) =>
          if (rep < threshold) Ok(negativeView.render(SoUser(name, imageUrl, soPage, rep, date)))
          else Ok(positiveView.render(SoUser(name, imageUrl, soPage, rep, date)))
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