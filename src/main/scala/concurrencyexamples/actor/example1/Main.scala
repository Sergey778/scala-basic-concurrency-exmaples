package concurrencyexamples.actor.example1

import akka.actor.{Actor, ActorSystem, Props}

/*
  Акторы - еще одна концепция паралеллизма.
  Актор - объект, который может получать и отправлять сообщения.
  Программа строится из взаймодействия акторов. Акторов может быть от одного до нескольких миллионов.
  Каждый актор может работать на пуле потоков, в отдельном потоке или даже на отдельном компьютере!
 */

// Чтобы создать актор требуется объявить его прототип
// В Scala это выглядит как создание класса
class Adder extends Actor {
  // У каждого актора объявлена функция receive, обрабатывающая входящие сообщения
  // Пока актор обрабатывает сообщения,
  // остальные сообщения отправленные этому потоку хранятся в очереди сообщений, называемой почтовым ящиком
  override def receive: Receive = {
    case (x: Int, y: Int) => // Объявляем, что актор может принимать кортеж из двух целых чисел
      println(Thread.currentThread.getName)
      sender ! x + y // sender - актор, который прислал обрабатываемое сообщение
  }
}

// Хорошей практикой считается создавать отдельный класс для описания сообщений
case class Add(x: Int, y: Int)

class Evaluator extends Actor {
  override def receive: Receive = {
    case Add(x, y) =>
      context.actorOf(Props[Adder]) ! (x, y) // Создаем новый актор и отправляем два числа
    case (x: Int) =>
      println(x)
  }
}

object Main extends App {

  // Чтобы создать актор - нужно создать систему акторов
  val actorSystem = ActorSystem("example")

  val eval = actorSystem.actorOf(Props[Evaluator]) // Конструкция для создания актора

  eval ! Add(10, 20) // Отправка сообщения
  eval ! Add(20, 30)
  eval ! Add(30, 40)

  actorSystem.terminate()

}

