package MyDispatch

/*
  AKKA dispater based on Java's Executor system, which provide asynchronous task
  execution environment.

  four dispatcher
  default : bind a set of actors to thread pool.
           each actor has own mailbox
           dispatcher share by actors.
           dispatcher can be implemented by thread pool or fork join.
  pinned : each actor has dedicated thread. usually for IO operation.
           dispatcher cannot be shared, implemented via thread pool, can't be fork join
  balancing : can redistribute event from busy actor to idle one. just for same type actors.
            implemented by fork join
  calling thread :  mostly for test usage. dispatcher run at current thread.


  four mailbox
  unbounded: queue size unlimited, via ConcurrentLinkedQueue
  bounded: queue size limited, via LinkedBlockingQueue
  unbounded priority : unlimited, via PriorityBlockingQueue
  bounded priority : limited, via akka.util.BoundedBlockingQueue via java PriorityBlockingQueue

  throughput : how many messages in mailbox handled before return thread to pool.

 */
object DefaultDispatcher {
  def main (args: Array[String]) {
     val dispatch_config =
       """
         my-dispatcher {
         type = Dispatcher
         executor = "fork-join-executor"
         fork-join-executor {
         parallelism-min = 2
         parallelism-factor = 2.0
         parallelism-max = 10
         }
         throughput = 100
         mailbox-capacity = -1
         mailbox-type = ""
       """.stripMargin
  }
}
