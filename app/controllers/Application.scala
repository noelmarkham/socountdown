package controllers

import play.api.mvc._
import service.{StackOverflowWebServiceProvider, StackOverflowProvider}
import play.api.templates.{Template1, Html}

class Application(provider: StackOverflowProvider,
                  positiveView: Template1[String, Html],
                  negativeView: Template1[String, Html]) extends Controller {
  
  def index = Action {
    Ok(positiveView.render("5"))
  }

}

object Application extends Application(StackOverflowWebServiceProvider, views.html.index, views.html.index)