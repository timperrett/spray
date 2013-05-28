package spray.metrics

import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import spray.http.HttpResponse
import spray.testkit.ScalatestRouteTest
import spray.routing.HttpService
import com.codahale.metrics.ConsoleReporter

class MetricDirectivesSpec extends FlatSpec
    with MustMatchers
    with HttpService
    with ScalatestRouteTest
    with MetricDirectives {

  def actorRefFactory = system

  val route =
    timeRequestResponse {
      path("foo" / "bar") {
        get {
          complete("ok")
        }
      }
    }

  it must "foo bar" in {
    Get("/foo/bar") ~> sealRoute(route) ~> check {
      println(" ************ THIS TEST IS TOTAL FOO BAR ************ ")
      // find a way to match against the out-of-band result
      entityAs[String] must be("ok")
    }
  }

  override def afterAll(conf: Map[String, _]) {
    ConsoleReporter.forRegistry(metrics).build().report()
  }

}
