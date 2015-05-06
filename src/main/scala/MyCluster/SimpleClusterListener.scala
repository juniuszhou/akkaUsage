package MyCluster

import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.actor.{Props, ActorSystem, ActorLogging, Actor}
import com.typesafe.config.ConfigFactory

class SimpleClusterListener extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart(): Unit = {
    //#subscribe
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
    //#subscribe
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent => // ignore
  }
}

object SimpleClusterListener {
  def main(args: Array[String]) {
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=8989")
    val system = ActorSystem.create("clusterSystem", config)
    system.actorOf(Props.create(SimpleClusterListener.getClass), "SimpleClusterListener")
  }
}


