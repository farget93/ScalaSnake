package cz.ctu.fit.kovart12.snake.util

import cz.ctu.fit.kovart12.snake.config.Config
import cz.ctu.fit.kovart12.snake.state.food.Food
import cz.ctu.fit.kovart12.snake.state.snake.{Body, Head}
import cz.ctu.fit.kovart12.snake.state.wall.Wall
import cz.ctu.fit.kovart12.snake.state.{GameAfterState, GameBeforeState, GameRunningState, State}
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.{Color, Paint}
import scalafx.scene.text.Font

class Renderer(graphicsContext2D: GraphicsContext) extends (State => State) {

  override def apply(state: State): State = {
    graphicsContext2D.clearRect(0, 0, state.config.screenWidth, state.config.screenHeight)
    state match {
      case state@GameRunningState(_, _, _, _, config) =>
        state.tiles.foreach {
          case Body(x, y) =>
            fillRectWithColor(x * config.scale, y * config.scale, config.scale, config.scale, Color.LightGrey)
          case Head(x, y) =>
            fillRectWithColor(x * config.scale, y * config.scale, config.scale, config.scale, Color.Grey)
          case Wall(x, y) =>
            fillRectWithColor(x * config.scale, y * config.scale, config.scale, config.scale, Color.Black)
          case Food(x, y) =>
            graphicsContext2D.fill = Color.Grey
            graphicsContext2D.fillOval((x + 0.25) * config.scale, (y + 0.25) * config.scale, config.scale / 2, config.scale / 2)
          case _ =>
        }
      case GameAfterState(score, config) =>
        printLines(config.screenWidth / 2, config.screenHeight / 2 - config.lineSize,
          List("GAME OVER", s"Score: ${score.score}", s"Distance: ${score.distance}"), config)
      case GameBeforeState(config) =>
        printLines(config.screenWidth / 2, config.screenHeight / 2 - config.lineSize,
          List("WELCOME", "Press WSAD for START"), config)
    }
    state
  }

  protected def fillRectWithColor(x: Double, y: Double, w: Double, h: Double, p: Paint) : Unit = {
    graphicsContext2D.fill = p
    graphicsContext2D.fillRect(x, y, w, h)
  }

  protected def printLines(x: Double, y: Double, lines : List[String], config : Config) : (Double, Double, Font) = {
    graphicsContext2D.fill = Color.Black
    lines.foldLeft((x, y, config.titleFont))((printData, line) => {
      graphicsContext2D.font = printData._3
      graphicsContext2D.fillText(line, printData._1, printData._2)
      (printData._1, printData._2 + config.lineSize, config.textFont)
    })
  }
}
