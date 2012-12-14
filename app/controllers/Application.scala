package controllers

import play.api.mvc._
import service.{StackOverflowWebServiceProvider, StackOverflowProvider}

class Application(provider: StackOverflowProvider) extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def scoreForUser(userId: Int): Int = {
    0
  }
}

object Application extends Application(StackOverflowWebServiceProvider)