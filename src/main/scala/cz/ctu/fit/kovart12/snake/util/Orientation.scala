package cz.ctu.fit.kovart12.snake.util

sealed trait Orientation {

  def apply(position : (Int, Int)) : (Int, Int) = ???
  def inverse : Orientation = ???

}

case class Up() extends Orientation{

  override def apply(position: (Int, Int)): (Int, Int) = (position._1, position._2 - 1)

  override def inverse: Orientation = Down()

}

case class Down() extends Orientation{

  override def apply(position: (Int, Int)): (Int, Int) = (position._1, position._2 + 1)

  override def inverse: Orientation = Up()

}

case class Left() extends Orientation{

  override def apply(position: (Int, Int)): (Int, Int) = (position._1 - 1, position._2)

  override def inverse: Orientation = Right()

}

case class Right() extends Orientation{

  override def apply(position: (Int, Int)): (Int, Int) = (position._1 + 1, position._2)

  override def inverse: Orientation = Left()

}