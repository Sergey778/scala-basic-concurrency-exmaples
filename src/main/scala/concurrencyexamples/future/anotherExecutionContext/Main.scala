package concurrencyexamples.future.anotherExecutionContext

import java.util.concurrent.{ExecutorService, Executors, ThreadPoolExecutor}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

// Future - сам по себе лишь обертка над вычисляемой функцией.
// Порядок вычисления, распределение по потокам и прочие подобные вещи зависят от штуки называемой ExecutionContext
// В предыдущих примерах мы использовали глобальный контекст(его создали за нас), передаваемый во Future не явно.
object Main extends App {

  // Здесь мы создаем свой контекст. Он будет работать на пуле потоков с константным количеством потоков(4).
  val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  // Примеры из предыдущей части, но с явно заданным контекстом исполнения
  val asyncSum = Future {
    println("Enter thread #1")
    20 + 10
  } (executionContext) // Чтобы не писать последнюю скобку мы могли бы объявить наш контекст неявным
  // Примерно так:
  // implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  val anotherSum = Future {
    println("Enter thread #2")
    30 + 20
  } (executionContext)

  val firstSum = Await.result(asyncSum, 5 seconds)
  val secondSum = Await.result(anotherSum, 5 seconds)

  println(firstSum + secondSum)
}

