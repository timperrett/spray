package spray.metrics

import com.codahale.metrics.{ Timer, MetricRegistry }
import spray.routing.Directive0
import spray.routing.directives.BasicDirectives

trait MetricDirectives {
  import BasicDirectives._

  val metrics: MetricRegistry = new MetricRegistry

  def timeRequestResponse: Directive0 =
    mapRequestContext { ctx ⇒
      val timer = metrics.timer("timer @ " + ctx.request.uri.toString) // TODO: make this name configurable via magnet
      val context = timer.time()
      ctx.withRouteResponseMapped { response ⇒
        context.stop()
        response
      }
    }

  def meterRequest: Directive0 =
    mapRequestContext { ctx ⇒
      metrics.meter("meter @ " + ctx.request.uri.toString).mark()
      ctx
    }
}
