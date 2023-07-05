package cz.ctu.fit.kovart12

import cz.ctu.fit.kovart12.snake.config.Config
import cz.ctu.fit.kovart12.snake.state.food.Food
import cz.ctu.fit.kovart12.snake.state.snake.{Head, Snake}
import cz.ctu.fit.kovart12.snake.state.stats.Stats
import cz.ctu.fit.kovart12.snake.state.wall.Wall
import cz.ctu.fit.kovart12.snake.state.{GameAfterState, GameBeforeState, GameRunningState, State}
import cz.ctu.fit.kovart12.snake.util.Orientation

import scala.concurrent.duration.{Duration, MILLISECONDS}

package object snake {

  val refreshRate = Duration.apply(80, MILLISECONDS)

  val initialStateFactory : Config => State = config => {
    val initialState : GameRunningState =
      GameRunningState(Snake(config.boardWidth / 2, config.boardHeight / 2, util.Right()), List(), List(), Stats(), config)

    Range(0, config.wallCount)
      .foldLeft(initialState)((s, _) => {
        GameRunningState(
          s.snake,
          s.food,
          s.wall.:::(s.emptyPosition(true).map(position => Wall(position)).toList),
          s.stats,
          s.config
        )
      })
  }

  val keyListener : (State, Orientation) => State = (state, orientation) => {
    state match {
      case GameBeforeState(config) => initialStateFactory(config)
      case GameRunningState(snake, food, wall, stats, config) =>
        GameRunningState(snake.withOrientation(orientation), food, wall, stats, config)
      case GameAfterState(_, config) =>
        initialStateFactory(config) match {
          case GameRunningState(snake, food, wall, stats, config) =>
            GameRunningState(snake.withOrientation(orientation), food, wall, stats, config)
          case state => state
        }
    }
  }

  val checkCollisions : State => State = {
    case state: GameRunningState =>
      state.tiles
        .filter(tile =>
          tile match {
            case Head(_, _) => false
            case _ => true
          })
        .find(tile => tile.position == state.snake.position)
        .map {
          case Food(x, y) =>
            GameRunningState(
              state.snake.grow(),
              state.food.filter(food => food.x != x || food.y != y),
              state.wall,
              state.stats.addScore(1),
              state.config
            )
          case _ => GameAfterState(state.stats, state.config)
        }
        .getOrElse(state)
    case a => a
  }

  val checkBounds : State => State = {
    case state@GameRunningState(snake, _, _, stats, config) =>
      List((0, config.boardWidth), (0, config.boardHeight))
        .zip(List(snake.x, snake.y))
        .map(zippedBound => zippedBound._2 < zippedBound._1._1 || zippedBound._2 >= zippedBound._1._2)
        .find(x => x) // Check if mapping resulted in true, if so return Option[True] otherwise None
        .map(_ => GameAfterState(stats, config))
        .getOrElse(state)
    case a => a
  }

  val createFood : State => State = {
    case state : GameRunningState =>
      GameRunningState(
        state.snake,
        (state.food ::: state.emptyPosition(false).map(position => Food(position)).toList).take(state.config.foodCount),
        state.wall,
        state.stats,
        state.config
      )
    case state => state
  }

  val move : State => State = {
    case GameRunningState(snake, food, wall, stats, config) =>
      GameRunningState(
        snake.move(snake.orientation),
        food,
        wall,
        stats.addDistance(1),
        config
      )
    case state => state
  }
}
