package TypedActor

import akka.actor.TypedActor.{PostStop, PreStart, Receiver}
import akka.actor.{ActorRef, ActorSystem, TypedActor, TypedProps}
import akka.event.Logging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// junius, from the example, we can see typed actor is used in the case of
// compatible with old system. communication via function call direct.
// unlike the untyped actor via mailbox or message.

trait CalaInt{
  def add(first: Int, second: Int): Future[Int]
  def sub(first: Int, second: Int): Future[Int]
  def incCount(): Unit
  def incAndReture(): Option[Int]
  // def onReceive(message: Any, sender: ActorRef): Unit
}

class Calculator extends CalaInt with Receiver {
  import akka.actor.TypedActor.context
  val log =  Logging(context.system, TypedActor.self.getClass())
  var count = 0

  // non blocking call, ask ?
  def add(first: Int, second: Int): Future[Int] = Future successful (first + second)
  def sub(first: Int, second: Int): Future[Int] = Future successful (first - second)

  // fire and forget call. !
  def incCount(): Unit = count += 1

  // blocking call.
  def incAndReture(): Option[Int] = {
    count += 1
    Some(count)
  }

  @Override def onReceive(message: Any, sender: ActorRef): Unit = {
    println(message.toString + " got from " + sender.toString())
    log.info("message got ")
  }
}

// for typed actor, need explicitly implement interface for lifecycle monitor.
class Calculator2(name: String) extends CalaInt with Receiver with PreStart with PostStop{
  var count = 0
  // non blocking call, ask ?
  def add(first: Int, second: Int): Future[Int] = Future successful (first + second)
  def sub(first: Int, second: Int): Future[Int] = Future successful (first - second)

  @Override
  def preStart(): Unit = {println("I am going to start.")}

  @Override
  def postStop(): Unit = {println("I am already stopped.")}

  @Override
  def onReceive(message: Any, sender: ActorRef): Unit = {println(message.toString + " got")}

  // fire and forget call. !
  def incCount(): Unit = count += 1

  // blocking call.
  def incAndReture(): Option[Int] = {
    count += 1
    Some(count)
  }
}


object MyTypedActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Typed")
    val calc: CalaInt = TypedActor(system).typedActorOf(TypedProps[Calculator]())
    // calc.onReceive("main")

    calc.incCount // get nothing
    val res: Future[Int] = calc.add(1,2)
    res.onSuccess {
      case i: Int => println(i)
    }

    val actref: ActorRef = TypedActor(system).getActorRefFor(calc)
    // for old version AKKA, tell just one parameter.
    actref.tell("hi there", null)

    // create actor with parameter.
    val calc2: CalaInt = TypedActor(system).typedActorOf(
        TypedProps(classOf[Calculator2], new Calculator2("junius")))

    // we can't use ! ? for typed actor. function call is the only communication channel.
    // calc2 ! ? " hello junius "


    val res2: Option[Int] = calc.incAndReture()
    println(res2.get)
    system.terminate()
  }

}
