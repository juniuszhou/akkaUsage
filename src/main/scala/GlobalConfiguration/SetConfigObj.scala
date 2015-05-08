package GlobalConfiguration

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * Created by junius on 15-5-8.
 */
object SetConfigObj {
  def main(args: Array[String]) {
    val customConf = ConfigFactory.parseString( """
                      akka.actor.deployment {
                      /my-service {
                      router = round-robin-pool
                      nr-of-instances = 3
                      }
                      }
                      """)

    val system = ActorSystem("Demo", ConfigFactory.load(customConf))
    println(system.name)
    system.shutdown()
  }
}
