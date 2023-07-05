package cz.ctu.fit.kovart12.snake.state.food

import cz.ctu.fit.kovart12.snake.Tile

object Food{

  def apply(position: (Int, Int)) : Food = Food(position._1, position._2)

}

case class Food(override val x: Int, override val y: Int) extends Tile{}