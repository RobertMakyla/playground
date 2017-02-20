package various

import org.scalatest.{FreeSpec, MustMatchers}

class HelloFreeSpec extends FreeSpec with MustMatchers {

  "nice table spec" - {

    List(
      Scenario(2, 1 + 1, "adding"),
      Scenario(2, 10 / 5, "dividing"),
      Scenario(2, 50 - 48, "substracting")
    ).foreach { scenario =>
      s"Operation of ${scenario.hint} must work" in {
        scenario.expected mustBe scenario.actual
      }
    }

  }

  case class Scenario(expected: Int, actual: Int, hint: String)

}
