package AkkaRawIO

import java.nio.ByteBuffer

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import akka.io.Tcp.Received
import akka.io.{ IO, Tcp }
import akka.util.{CompactByteString, ByteString}
import java.net.InetSocketAddress
import akka.io.{ IO, Tcp }

import scala.collection.immutable.Iterable

class SimpleServerHandler extends Actor{
  def receive = {
    case d @ Received(data) => {
      println("server got data")
      println(data)
    }
    case _ => println("default")
  }
}
class TcpServer extends Actor{
  import Tcp._
  import context.system

  val address = new InetSocketAddress("localhost", 8088)

  IO(Tcp) ! Bind(self, address)

  def receive = {
    case b @ Bound(localAddress) => {
      println("server, a client is connecting")
    }

    case c @ Connected(remote, local) => {
      println("server, a client connected")
      val handler = context.actorOf(Props[SimpleServerHandler])
      sender() ! Register(handler)
      val b = CompactByteString("you are connected.")
      sender() ! Write(b)
    }

    case d @ Received(data) => {
      println("server got data")
      println(data)
    }
  }
}

object TcpServer extends App{
  val system = ActorSystem("Tcp")
  val server = system.actorOf(Props[TcpServer])
}
