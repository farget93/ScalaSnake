package cz.ctu.fit.kovart12.snake.state.wall

import cz.ctu.fit.kovart12.snake.Tile

object Wall{

  def apply(position : (Int, Int)) : Wall = Wall(position._1, position._2)

}

case class Wall(override val x: Int, override val y: Int) extends Tile{}
