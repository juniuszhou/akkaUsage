package BasicActor

import akka.actor.SupervisorStrategy.Restart
import akka.actor._

import scala.concurrent.duration._
/*
  one for one, if an actor is down, it supervisor just restart it.
  all for one, if an actor is down, it supervisor restart all supervisor's child actor.
 */

class ParentActor extends Actor {
  val childOne = context.actorOf(Props[ChildActor], "childone")
  val childTwo = context.actorOf(Props[ChildActor], "childtwo")

  override def receive: Receive = {
    case "crash" => childOne ! "crash"
    case _ => println("got message")
  }

  /*
  override val supervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _ => Restart
  }
  */

  override val supervisorStrategy = AllForOneStrategy(
    maxNrOfRetries = 10, withinTimeRange = 10 seconds) {
    case _ => Restart
  }
}

class ChildActor extends Actor {
  override def preStart(): Unit = {println("start up " + this.toString)}
  override def postStop(): Unit = {println("stopped " + this.toString)}

  override def receive: Receive = {
    case "crash" => throw new Exception
    case _ => println("got message")
  }
}

object SuperviseActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[ParentActor], name = "Actor1")

    // send crash to parent then forward to child. child will exception, then parent
    // will restart this child or all children according to strategy.
    actor1 ! "crash"

    Thread.sleep(1000)

  }
}
