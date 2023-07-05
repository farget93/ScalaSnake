package cz.ctu.fit.kovart12.snake.state

import cz.ctu.fit.kovart12.snake.Tile
import cz.ctu.fit.kovart12.snake.config.Config
import cz.ctu.fit.kovart12.snake.state.food.Food
import cz.ctu.fit.kovart12.snake.state.snake.{Head, Snake}
import cz.ctu.fit.kovart12.snake.state.stats.Stats
import cz.ctu.fit.kovart12.snake.state.wall.Wall

import scala.math.abs
import scala.util.Random

sealed trait State{

  val config : Config

}

case class GameRunningState(
                         snake: Snake,
                         food : List[Food],
                         wall: List[Wall],
                         stats: Stats,
                         override val config: Config
                       ) extends State {

  def tiles : List[Tile] = snake.body.:::(food).:::(wall)

  def emptyPosition(guarded : Boolean) : Option[(Int, Int)] = {
    val position = (Random.nextInt(config.boardWidth), Random.nextInt(config.boardHeight))
    // Fold on option type
    // if present than map function is used
    // if missing than fold initial argument is used
    tiles.find(tile => {
      tile match {
        case Head(x, y) => guarded && (abs(x - position._1) + abs(y - position._2)) < 6
        case tile => tile.position == position
      }
    }).fold(Option.apply(position))(_ => Option.empty)
  }

}

case class GameAfterState(stats: Stats, override val config: Config) extends State {}
case class GameBeforeState(override val config: Config) extends State{}