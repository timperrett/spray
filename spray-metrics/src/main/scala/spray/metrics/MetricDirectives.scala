package spray.metrics

import com.codahale.metrics.{ Timer, MetricRegistry }
import spray.routing.Directive0
import spray.routing.directives.BasicDirectives

trait MetricDirectives {
  import BasicDirectives._

  val metrics: MetricRegistry = new MetricRegistry

  def timeRequestResponse: Directive0 =
    mapRequestContext { ctx ⇒
      val timer = metrics.timer("response timer at " + ctx.request.uri.toString) // TODO: make this name configurable via magnet
      val context = timer.time()
      ctx.withRouteResponseMapped { response ⇒
        context.stop()
        response
      }
    }

}
