package cz.ctu.fit.kovart12.snake.state.snake

import cz.ctu.fit.kovart12.snake.Tile

trait SnakePart extends Tile {

  def move(position: (Int, Int)): SnakePart = ???

}

case class Body(override val x: Int, override val y: Int) extends SnakePart {

  override def move(position: (Int, Int)): Body = Body(position._1, position._2)

}

case class Head(override val x: Int, override val y: Int) extends SnakePart{

  override def move(position: (Int, Int)): Head = Head(position._1, position._2)

}