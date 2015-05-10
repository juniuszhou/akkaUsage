package AkkaRawIO

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress
import akka.io.{IO, Tcp}

class LocalListener extends Actor {
  def receive = {
    case s: String => println(s)
    case _ => println("default ")
  }
}

object TcpClient {
  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[TcpClient], remote, replies)

  def main(args: Array[String]) {
    val system = ActorSystem("Tcp")

    //new a supervise actor. then if anything happened in child actor.
    //we can follow. Otherwise main function know nothing since it is
    // not an actor and no receive method.
    val listener = system.actorOf(Props[LocalListener])
    val address = new InetSocketAddress("localhost", 8088)
    val client = system.actorOf(TcpClient.props(address, listener))
    //system.shutdown()
  }
}

class TcpClient(remote: InetSocketAddress, listener: ActorRef) extends Actor {

  import Tcp._
  import context.system

  // IO(Tcp) to get IO driver for tcp protocol. each protocol has a manager in IO
  //
  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    case c @ Connected(remote, local) =>
      println("connection is established")
      val remoteAddr = c.remoteAddress
      val localAddr = c.localAddress
      println(remote, remoteAddr, local, localAddr)
      listener ! c
      val connection = sender()

      //register self as actor to response following message.
      // or you can init a new actor to do it. then this one just supervise it.
      connection ! Register(self)
      context become {
        case data: ByteString =>
          println("we got data from server")
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          listener ! "write failed"
        case Received(data) =>
          println("we got data from server")
          println("data is " + data.toString())
          listener ! data
        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }
}
