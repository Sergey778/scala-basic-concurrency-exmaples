package concurrencyexamples.future.example3

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits._

// Посмотрим на более жизненный (лишь слегка) пример использования Future

object Main extends App {

  val users = List("John", "Henry", "Bonem")

  // Будем брать случайного пользователя из списка
  def getUser = Future {
    Random.shuffle(users).head
  }

  // Делать последнюю букву имени заглавной
  def capitalizeLastLetter(userName: String) = Future {
    userName.take(userName.length - 1) + userName.last.toUpper
  }

  // И добавлять случайную фамилию
  def addSurname(userName: String) = Future {
    userName + " " + Random.nextString(5)
  }

  // Так мы можем объединить все наши действия в список действий/поток вычислений
  val computationStream = getUser flatMap capitalizeLastLetter flatMap addSurname

  // Или это можно записать так
  val alternativeStream = for {
    userName <- getUser
    capitalized <- capitalizeLastLetter(userName)
    result <- addSurname(capitalized)
  } yield result

  // Подождем пока действия выполнятся и распечатаем людей со странными фамилиями
  println(Await.result(computationStream, Duration.Inf))
  println(Await.result(alternativeStream, Duration.Inf))
}

/*
  Итак подведем итоги:
  Future - удобная и легковесная концепция многопоточного программирования, особенно когда этого многопоточного программирования много!
  Плюсы:
  + Удобно использовать
  + Удобно конфигурировать
  + Маленький оверхед над потоками
  Минусы:
  - Неудобно когда есть изменяющиеся данные
  - Future + блокировки = боль
  - Можно испугаться, что Future - монада. А МОНАДЫ ЭТО СТРАШНО И МАТАН!
 */