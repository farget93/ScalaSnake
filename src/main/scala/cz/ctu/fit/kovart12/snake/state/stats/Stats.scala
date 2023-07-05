package cz.ctu.fit.kovart12.snake.state.stats

object Stats{

  def apply() : Stats = Stats(0, 0)

}

case class Stats(distance: Int, score: Int) {

  def addDistance(distance: Int): Stats = Stats(this.distance + distance, score)
  def addScore(score: Int): Stats = Stats(distance, this.score + score)

}
