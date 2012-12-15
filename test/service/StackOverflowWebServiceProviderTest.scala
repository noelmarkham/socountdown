package service

import org.specs2.mutable._
import java.util.{GregorianCalendar, Date}

class StackOverflowWebServiceProviderTest extends Specification {
  "The date calculator" should {
    "return none for a too high score" in {
      val value = StackOverflowWebServiceProvider.projectedDate(2000000, new Date())
      value match {
        case None => true
        case Some(x) => failure("calculation produced a projected date: [%s]".format(x))
      }
    }

    "produce a date in the future for a real calculation" in {
      val nowCal = new GregorianCalendar(2010, 6, 2)
      val value = StackOverflowWebServiceProvider.projectedDate(300000, nowCal.getTime)
      value match {
        case None => failure("Didn't produce a future date for score")
        case Some(x) if x.before(nowCal.getTime) => failure("Produced a date in the past: [%s]".format(x))
        case Some(x) => {
          println("Received date [%s]".format(x))
          true
        }
      }
    }
  }
}
