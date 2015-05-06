/**
 * Created by junius on 14-12-6.
 */
package BasicActor

import akka.actor._
import akka.event.Logging

/*
General Tips for AKKA usage.

要尽量避免在一个 actor 中关闭另一个 actor,这可能会引起 Bug 和条件竞争。
! 消息一定要采用不可变的对象,在 scala 中不可变对象有 String、Int、Double
等的对象,还有 case class 类型的对象,以及 scala.Tuple2、scala.List 和
scala.Map 类型的对象。
! 出于性能的考虑,发送消息尽量采用“!”(tell),除非有必要才使用“?”
(ask)。
! 由于 AKKA 邮件先进先出的机制,任何系统或配置产生的系统消息,未必
会第一时间被 receive 方法处理,可能排在其它消息后面被处理。
! 初始化 Actor 可以通过消息的方式,配合 stack、become 等方法,但是要尽
量避免使用。
! 开发 Actor 程序时,一般采用 one-for-one 容错机制,有特殊需求才使
用 all-for-one 容错机制。
! 尽量避免使用除 actor 以外的同步机制,例如 sychronized、 thread 和 lock 等。
! Actor 类最好是无状态的。

 */
private class Actor1 extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
  }
}

// 构造器参数不能是可变的(var),因为 call-by-name 块可能被其它线程调用,引起条件竞争。
private class Actor2(name: String) extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case "test" => println("test received")
    case _ => log.info("unknown message")
  }
}

class WatchActor(val other: ActorRef) extends Actor {

  context.watch(other)
  var lastSender = context.system.deadLetters

  def receive = {
    case Terminated(other) => println(other.toString + " watched dead.")
  }
}

object SimpleActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")
    val actor1 = system.actorOf(Props[Actor1], name = "Actor1")
    actor1 ! "test"
    actor1 ! "other"

    // how to create actor with parameter.
    val actor2 = system.actorOf(Props(new Actor2("actor2")), name = "Actor2")
    actor2 ! "test"
    actor2 ! "other"

    val watchActor = system.actorOf(Props(new WatchActor(actor2)), name = "WatchActor")

  }
}
