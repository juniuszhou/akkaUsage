import java.net.InetSocketAddress

import AkkaRawIO.TcpClient

object MyTcp{
  def main (args: Array[String]) {
    //addr: InetAddress, port: Int
    val address = new InetSocketAddress("www.baidu.com", 80)
    //val client = new TcpClient(address, )
  }
}