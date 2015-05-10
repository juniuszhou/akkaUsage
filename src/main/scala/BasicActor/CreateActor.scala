package BasicActor

import akka.actor.{Actor, Props}

/**
 * It is a good idea to provide factory methods on the companion object of each Actor which help keeping the
creation of suitable Props as close to the actor definition as possible. This also avoids the pitfalls associated with
using the Props.apply(...) method which takes a by-name argument, since within a companion object the
given code block will not retain a reference to its enclosing scope:
 */
object CreateActor {
  def props(name: String): Props = Props(new CreateActor(name))
}

class CreateActor(name: String) extends Actor {
  def receive = {
    case _ => println("got message")
  }
}

class OutSideActor extends Actor {
  val other = context.actorOf(CreateActor.props("demo"), "other")
  def receive = {
    case _ => println("create other actor")
  }
}
