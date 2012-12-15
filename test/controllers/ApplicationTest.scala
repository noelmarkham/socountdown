package controllers

import org.specs2.mutable._
import org.specs2.mock.Mockito
import service.{SoUser, StackOverflowProvider}
import play.api.templates.{Template1, Html}
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.concurrent.Promise
import play.api.mvc.AsyncResult

class ApplicationTest extends Specification with Mockito {

  "The controller" should {
    "put the correct value in the model for the default user in the negative case" in {

      val defaultUser = 1
      val expectedScore = 100

      val dummyUser = SoUser("name", "image_url", "so_url", expectedScore, "creation_date")

      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(defaultUser) returns Promise.pure(dummyUser)

      val mockPositiveView = mock[Template1[SoUser, Html]]
      val mockNegativeView = mock[Template1[SoUser, Html]]

      mockPositiveView.render(any[SoUser]) answers {u => Html(u.toString)}
      mockNegativeView.render(any[SoUser]) answers {u => Html(u.toString)}

      val controller = new Application(mockProvider, 1, 500, mockPositiveView, mockNegativeView)
      val AsyncResult(responseFromController) = controller.index(FakeRequest())

      status(await(responseFromController)) must equalTo (OK)
      contentAsString(await(responseFromController)) must equalTo (dummyUser.toString)

      there was one(mockProvider).getScoreForUser(defaultUser)
      there was one(mockNegativeView).render(dummyUser)
      there was no(mockPositiveView).render(any[SoUser])
    }

    "put the correct value in the model for the default user in the positive case" in {

      val defaultUser = 1
      val expectedScore = 5000

      val dummyUser = SoUser("name", "image_url", "so_url", expectedScore, "creation_date")

      val mockProvider = mock[StackOverflowProvider]
      mockProvider.getScoreForUser(defaultUser) returns Promise.pure(dummyUser)

      val mockPositiveView = mock[Template1[SoUser, Html]]
      val mockNegativeView = mock[Template1[SoUser, Html]]

      mockPositiveView.render(any[SoUser]) answers {u => Html(u.toString)}
      mockNegativeView.render(any[SoUser]) answers {u => Html(u.toString)}

      val controller = new Application(mockProvider, 1, 500, mockPositiveView, mockNegativeView)
      val AsyncResult(responseFromController) = controller.index(FakeRequest())

      status(await(responseFromController)) must equalTo (OK)
      contentAsString(await(responseFromController)) must equalTo (dummyUser.toString)

      there was one(mockProvider).getScoreForUser(defaultUser)
      there was one(mockPositiveView).render(dummyUser)
      there was no(mockNegativeView).render(any[SoUser])
    }
  }
}
