package AkkaRawIO

import akka.actor.{ Actor, ActorRef, Props }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress
import akka.io.{ IO, Tcp }

object TcpClient {
  def props(remote: InetSocketAddress, replies: ActorRef) =
  Props(classOf[TcpClient], remote, replies)
}
class TcpClient(remote: InetSocketAddress, listener: ActorRef) extends Actor{
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
      listener ! c
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          listener ! "write failed"
        case Received(data) =>
          listener ! data
        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self
      }
  }
}
