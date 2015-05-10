package AkkaRawIO

import java.net.InetSocketAddress

import akka.actor.{Actor, Props, ActorSystem}
import akka.io.{IO, Tcp}
import akka.util.CompactByteString


class AckBasedWrite extends Actor {

  import context.system
  import Tcp._

  case object Ack extends Event

  val remote = new InetSocketAddress("localhost", 8088)
  IO(Tcp) ! Connect(remote)

  def receive = {
    case c@Connected(remote, local) =>
      println("connection is established")
      sender() ! Register(self)
      context become {
        case Received(data: CompactByteString) =>
          println("data from server")
          //println(data.map(b => b.toChar))

          //correct method convert compact bytestring to string.
          println(data.utf8String)
          sender() ! Write(data, Ack)
        case PeerClosed =>
          println("peer closed.")
        case data =>
          //after data written successfully, we will an Ack method.
          println("got data from server as: ", data)
      }
  }
}

object AckBasedWrite {
  def main(args: Array[String]) {
    val system = ActorSystem("Tcp")

    val listener = system.actorOf(Props[LocalListener])

    val client = system.actorOf(Props[AckBasedWrite])
    //system.shutdown()
  }
}
