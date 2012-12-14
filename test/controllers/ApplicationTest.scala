package controllers

import org.specs2.mutable._
import org.specs2.mock.Mockito
import service.StackOverflowProvider
import play.api.templates.{Template1, Html}
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Result

class ApplicationTest extends Specification with Mockito {

  "The controller" should {
    "put the correct value in the model" in {
      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(1) returns 100

      val mockPositiveView = mock[Template1[String, Html]]
      val mockNegativeView = mock[Template1[String, Html]]

      mockPositiveView.render("5") returns Html("5")

      val controller = new Application(mockProvider, mockPositiveView, mockNegativeView)
      val responseFromController:Result = controller.index(FakeRequest())

      status(responseFromController) must equalTo (OK)
      val response = contentAsString(responseFromController)
      response must equalTo ("5")
    }
  }
}
