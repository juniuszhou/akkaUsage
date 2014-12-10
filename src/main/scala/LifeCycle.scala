/**
 * Created by junius on 14-12-7.
 */
import akka.actor.Actor
import akka.util.Timeout
import scala.concurrent.duration

class LifeActor extends Actor {
  context.setReceiveTimeout(30, scala.concurrent.duration.MILLISECONDS)


  // just called once when created at first time.
  override def preStart() {}

  // mailbox can receive message when restart.
  override def preRestart(reason: Throwable, message: Option[Any]): Unit ={
    super.preRestart(reason, message)
  }

  // mailbox can't work when postStop. method just used for release resource.
  override def postStop() {}

  def receive = {
    case _ =>
  }

  // if message not handled in receive, then this method will handle it. except Terminated.
  override def unhandled(message: Any): Unit ={
    super.unhandled(message)
  }

}
object LifeCycle {
  def main (args: Array[String]) {

  }
}
