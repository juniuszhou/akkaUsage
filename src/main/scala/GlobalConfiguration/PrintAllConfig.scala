package GlobalConfiguration

import akka.actor.ActorSystem

// not just akka, all components from typesafe use config library.
// https://github.com/typesafehub/config
object PrintAllConfig {
  def main (args: Array[String]) {
    val system = ActorSystem.create("Demo")
    // a lots of output
    println(system.settings)
    system.shutdown()
  }
}
