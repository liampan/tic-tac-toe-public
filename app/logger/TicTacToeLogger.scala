package logger

import com.google.inject.{Inject, Singleton}
import org.joda.time.DateTime

@Singleton
class TicTacToeLogger @Inject()(
                      dateTime: DateTime
                      ) {

  def info(message: Any): Unit ={
    val messageToLog:String =  s"[info] ${DateTime.now()} TicTacToe $message"
    println(messageToLog)
  }

}
