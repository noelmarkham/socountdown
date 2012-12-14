package controllers

import org.specs2.mutable._
import org.specs2.mock.Mockito

class ApplicationTest extends Specification with Mockito {

  "The controller" should {
    "return true in simple case" in {
      val x = 1
      x should equalTo (1)
    }
  }
}
