package BasicActor

import akka.actor.Actor

/**
 * Created by junius on 15-5-9.
 */
class ActorInbox extends Actor{
  //implicit val i = inbox()
  def receive = {
    case _ => println()
  }
}
object ActorInbox {
  def main (args: Array[String]) {
    //implicit val i = inbox()

  }
}
