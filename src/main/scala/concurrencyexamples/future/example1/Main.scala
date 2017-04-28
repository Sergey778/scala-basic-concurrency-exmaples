package concurrencyexamples.future.example1

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits._

/*
  Future - объект, который хранит результат вычисляемого действия.
  При создании мы задаем некоторое действие, которое должно выполняться в отдельном потоке.
  Сам Future может иметь три состояния:
  - Действие еще выполняется(Результат не готов)
  - Действие выполнено(Результат готов)
  - Действие выполнено с ошибкой(В этом случае объект хранит информацию об ошибке)
 */

object Main extends App {

  // Создать Future очень просто. В отдельном потоке данный Future выполнит сложение 20 и 10.
  val asyncSum = Future {
    println("Enter thread #1")
    20 + 10
  }

  val anotherSum = Future {
    println("Enter thread #2")
    30 + 20
  }

  // Await - специальный объект, используемый для ожидания результата Future. В реальных проектах почти не используется
  // Также, мы можем ограничить время ожидания. Если Future не вычисляется за заданное время происходит исключение
  val firstSum = Await.result(asyncSum, 5 seconds)
  val secondSum = Await.result(anotherSum, 5 seconds)

  println(firstSum + secondSum)
}

