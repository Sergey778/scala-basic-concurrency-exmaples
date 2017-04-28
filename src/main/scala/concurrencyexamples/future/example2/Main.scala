package concurrencyexamples.future.example2

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


object Main extends App {

  /*
    Основная идея Future - описание списка действий, выполняемых в отдельном потоке/потоках
    На данном примере можно увидеть как происходит описание
   */
  val sumActions = Future(10 + 20) // Создаем Future
    .map(_ + 30) // Это действие выполнится когда будет посчитано 10 + 20
    .filter(_ < 50) // Действия, последующие за этим выполняем только если результат предыдущего меньше 50
    .flatMap(x => Future(x + 50))
  // Это действие аналогично map за исключением того, что функция вернет не Future[Future], а Future

  // Также можно совершить, что-то с итоговым результатом
  sumActions onComplete {
    case Success(_) => println("Yep. Smaller than 50") // Обрабатываем успешное вычисление
    case Failure(error) => println(error.getMessage) // Обрабатываем ошибки
  }

  Await.ready(sumActions, Duration.Inf)
}
