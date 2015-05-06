package BasicActor

import akka.actor.ActorSystem

/**
 * Created by junius on 14-12-13.
 */
object SearchActor {
  def main (args: Array[String]) {
    val system = ActorSystem("Demo")

    // actorFor method was deprecated.

    // actorOf always create a new actor
    // actorSelection just search an existing actor. local or remote.

    /* different actor path.
    "akka://my-system/user/service-a/worker1"               // 纯本地
    "akka://my-system@serv.example.com:5678/user/service-b" // 本地或远程
    "cluster://my-cluster/service-c"                       // 集群 (未来扩展)

    "/user" 是所有由用户创建的顶级actor的监管者，用 ActorSystem.actorOf 创建的actor在其下一个层次 are found at the next level.
    "/system" 是所有由系统创建的顶级actor（如日志监听器或由配置指定在actor系统启动时自动部署的actor）的监管者
    "/deadLetters" 是死信actor，所有发往已经终止或不存在的actor的消息会被送到这里
    "/temp" 是所有系统创建的短时actor(i.e.那些用在ActorRef.ask的实现中的actor)的监管者.
    "/remote" 是一个人造的路径，用来存放所有其监管者是远程actor引用的actor
     */
    val actor1 = system.actorSelection("akka://Demo/user")
    actor1 ! "hello"

  }

}
