package cz.ctu.fit.kovart12.snake.config

import cz.ctu.fit.kovart12.snake.refreshRate
import scopt.{OParser, OParserBuilder}

import scala.concurrent.duration.Duration
import scala.util.Try

object ConfigParser {

  val builder = OParser.builder[Config]
  val parser = {
    import builder._
    OParser.sequence(
      programName("Snake"),
      opt[Int]('w', "width")
        .action((width, config) => config.copy(boardWidth = width))
        .validate(width => {
          if(width < 15)
            failure("Width can not be smaller than 15")
          else
            success
        })
        .text("Optional board width, default is 31"),
    opt[Int]('h', "height")
      .action((height, config) => config.copy(boardHeight = height))
      .validate(height => {
        if(height < 15)
          failure("Height can not be smaller than 15")
        else
          success
      })
      .text("Optional board height, default is 31"),
    opt[Int]('s', "scale")
      .action((scale, config) => config.copy(scale = scale))
      .validate(scale => {
        if(scale < 8)
          failure("Scale can not be smaller than 8")
        else
          success
      })
      .text("Optional board height, default is 16"),
    opt[Int]("wall")
      .action((wall, config) => config.copy(wallCount = wall))
      .validate(wall => {
        if(wall < 0)
          failure("Wall count can not be smaller than 0")
        else
          success
      })
      .text("Optional wall count, default is max of width and height"),
    opt[Int]("food")
      .action((food, config) => config.copy(foodCount = food))
      .validate(food => {
        if(food < 0)
          failure("Food count can not be smaller than 0")
        else
          success
      })
      .text("Optional food count, default is 3"),
      opt[String]('t', "time")
        .action((time : String, config) => config.copy(time = Try(Duration.apply(time))
          .getOrElse(refreshRate)))
        .validate((time : String ) => {
          val duration : Duration = Try(Duration.apply(time))
            .getOrElse(refreshRate)
          if(duration < refreshRate)
            failure(s"Time can not be smaller than ${refreshRate.toString()}")
          else
            success
        })
        .text("Logic refresh rate, default is 120 ms")
    )
  }
}
