package cz.ctu.fit.kovart12.snake

trait Tile {
  def x: Int = ???
  def y: Int = ???

  val position : (Int, Int) = (x, y)
}