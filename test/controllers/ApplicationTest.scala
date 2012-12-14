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
    "put the correct value in the model for the default user in the negative case" in {

      val defaultUser = 1
      val expectedScore = 100

      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(defaultUser) returns expectedScore

      val mockPositiveView = mock[Template1[Int, Html]]
      val mockNegativeView = mock[Template1[Int, Html]]

      mockPositiveView.render(anyInt) answers {i => Html(i.toString)}
      mockNegativeView.render(anyInt) answers {i => Html(i.toString)}

      val controller = new Application(mockProvider, 1, 500, mockPositiveView, mockNegativeView)
      val responseFromController:Result = controller.index(FakeRequest())

      status(responseFromController) must equalTo (OK)
      contentAsString(responseFromController) must equalTo (expectedScore.toString)

      there was one(mockProvider).getScoreForUser(defaultUser)
      there was one(mockNegativeView).render(expectedScore)
      there was no(mockPositiveView).render(anyInt)
    }

    "put the correct value in the model for the default user in the positive case" in {

      val defaultUser = 1
      val expectedScore = 5000

      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(defaultUser) returns expectedScore

      val mockPositiveView = mock[Template1[Int, Html]]
      val mockNegativeView = mock[Template1[Int, Html]]

      mockPositiveView.render(anyInt) answers {i => Html(i.toString)}
      mockNegativeView.render(anyInt) answers {i => Html(i.toString)}

      val controller = new Application(mockProvider, 1, 500, mockPositiveView, mockNegativeView)
      val responseFromController:Result = controller.index(FakeRequest())

      status(responseFromController) must equalTo (OK)
      contentAsString(responseFromController) must equalTo (expectedScore.toString)

      there was one(mockProvider).getScoreForUser(defaultUser)
      there was one(mockPositiveView).render(anyInt)
      there was no(mockNegativeView).render(expectedScore)
    }
  }
}
