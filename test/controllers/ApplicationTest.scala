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
    "put the correct value in the model for the default user" in {

      val defaultUser = 1
      val expectedScore = 100

      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(defaultUser) returns expectedScore

      val mockPositiveView = mock[Template1[Int, Html]]
      val mockNegativeView = mock[Template1[Int, Html]]

      mockPositiveView.render(anyInt) answers {i => Html(i.toString)}

      val controller = new Application(mockProvider, mockPositiveView, mockNegativeView)
      val responseFromController:Result = controller.index(FakeRequest())

      status(responseFromController) must equalTo (OK)
      val response = contentAsString(responseFromController)
      response must equalTo (expectedScore.toString)
    }
  }
}
