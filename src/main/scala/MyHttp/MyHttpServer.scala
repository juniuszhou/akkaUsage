package MyHttp

import akka.actor.{Actor, ActorSystem}
import akka.http.scaladsl.model.{Uri, HttpResponse, HttpRequest}

import akka.http.impl.util.JavaMapping.HttpRequest
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.io.IO
import akka.stream.scaladsl.Flow
import scala.concurrent.Future

//class MyHttpServer extends Actor{
  //IO(Http) !
//}

object MyHttpServer {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("Streams")
    //implicit val materializer = FlowMaterializer()

    // start the server on the specified interface and port.
    //val serverBinding2 = Http().bind(interface = "localhost", port = 8091)


  }
}
