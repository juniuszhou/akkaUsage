package MyDispatch

import akka.actor.{Props, ActorSystem, Actor}
import akka.routing._
import akka.util.Timeout
import scala.language.postfixOps
import scala.concurrent.duration._

/*
  router is also a type of actor. responsible for route message to actor.
  round robin
  random
  smallest
  broadcast
  scatter

  router encapsulate interfaces in trait, customer can define its own router.
 */

class PrintlnActor extends Actor {
  def receive = {
    case msg ⇒
      println("Received message '%s' in actor %s".format(msg, self.path.name))
  }
}

object DefaultRouter {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val roundRobinRouter =
      system.actorOf(RoundRobinPool(5).props(Props[PrintlnActor]), "rounter1")
      // following is old interface, deprecated.
      // system.actorOf(Props[PrintlnActor].withRouter(RoundRobinRouter(5)), "router")

    // each actor got the same possibility to be called and got message.
    // but in general, each actor will be even called.
    1 to 10 foreach {
      i ⇒ roundRobinRouter ! i
    }

    val randomRouter = system.actorOf(Props[PrintlnActor].withRouter(RandomPool(5)), "router2")
    // totally random to choose actor to send message.
    1 to 10 foreach {
      i ⇒ randomRouter ! i
    }

    val smallestRouter = system.actorOf(Props[PrintlnActor].withRouter(SmallestMailboxPool(5)), "router3")
    // priority like this, 1. actor not dealing message 2. empty actor 3. less message actor 4. remote actor.
    1 to 10 foreach {
      i ⇒ smallestRouter ! i
    }

    val broadCastRouter = system.actorOf(Props[PrintlnActor].withRouter(BroadcastPool(5)), "router4")
    // send to all.
    1 to 10 foreach {
      i ⇒ broadCastRouter ! i
    }

    val scatterGatherFirstCompleteRouter =
      system.actorOf(Props[PrintlnActor].withRouter(ScatterGatherFirstCompletedPool(
        nrOfInstances = 5, within = 2 seconds)), "router5")
    implicit val timeout = Timeout(5 seconds)
    1 to 10 foreach {
      i ⇒ scatterGatherFirstCompleteRouter ! i
    }

  }
}
