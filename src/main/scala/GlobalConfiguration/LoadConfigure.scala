package GlobalConfiguration

import com.typesafe.config.ConfigFactory

object LoadConfigure {
  def main (args: Array[String]) {
    val config = ConfigFactory.load()
    val ite = config.entrySet.iterator()
    while (ite.hasNext){
      val m = ite.next()
      if(m.getKey.contains("serializers")) println(m.getKey, m.getValue)
    }
  }
}
