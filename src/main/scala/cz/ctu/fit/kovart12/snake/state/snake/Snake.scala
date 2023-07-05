package cz.ctu.fit.kovart12.snake.state.snake

import cz.ctu.fit.kovart12.snake.util.Orientation

object Snake {

  def apply(x: Int, y: Int, orientation: Orientation) : Snake = Snake(x, y, orientation, List(Head(x, y)))

}

case class Snake(x: Int, y: Int, orientation: Orientation, body: List[SnakePart]) {

  val position : (Int, Int) = (x, y)

  def withOrientation(orientation: Orientation) : Snake = {
    if(body.size > 1 && orientation == this.orientation.inverse)
      Snake(x, y, this.orientation, body)
    else
      Snake(x, y, orientation, body)
  }

  def move(orientation: Orientation) : Snake = {
    val headPosition = orientation.apply((x, y))
    Snake(
      headPosition._1,
      headPosition._2,
      orientation,
      body.foldRight((List[SnakePart](), headPosition))((tile, result) => {
        (result._1.::(tile.move(result._2)), (tile.x, tile.y))
      })._1
    )
  }

  def grow() : Snake = {
    // Grow is ensured by adding new body part to snake chain, placed outside visible area
    // Position for newly added part is determined during move
    Snake(x, y, orientation, body.::(Body(-1, -1)))
  }
}
