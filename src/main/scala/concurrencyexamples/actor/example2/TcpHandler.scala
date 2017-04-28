package concurrencyexamples.actor.example2

import java.net.InetSocketAddress

import akka.actor.Actor
import akka.io.{IO, Tcp}
import akka.io.Tcp._


class TcpHandler extends Actor {

  // Соединения по сети также можно представить как актора
  val io = IO(Tcp)

  // Также акторы позволяют хранить внутри себя изменяемые данные без необходимости думать о блокировках
  var count = 0

  // Все команды также с помощью отправки сообщений
  io ! Bind(self, new InetSocketAddress("localhost", 5432))

  override def receive: Receive = {
    case Received(data) => // Данные, приходящие по сети, тоже приходят в сообщении
      count += 1
      io ! Write(data)
  }
}

/*
  Итоги:
  Акторы - мощная концепция, заставляющая строить приложения в полностью параллельной манере
  Плюсы:
  + Легкое масштабирование приложения
  + Удобное использование мутабельных данных
  Минусы:
  - Требует особого подхода к написанию приложений
  - Неправильное конфигурирование в корне убивает производительность
 */