package cz.ctu.fit.kovart12.snake.config

import cz.ctu.fit.kovart12.snake.refreshRate
import scalafx.scene.text.Font

import scala.concurrent.duration.Duration
import scala.math.ceil

case class Config(
                   boardWidth: Int = 31,
                   boardHeight: Int = 31,
                   scale: Int = 16,
                   wallCount: Int = 20,
                   foodCount: Int = 3,
                   time: Duration = refreshRate
                 ) {

  val screenWidth : Int = scale * boardWidth
  val screenHeight : Int = scale * boardHeight

  val lineSize : Double = ceil(screenHeight / 10)
  val titleFont = new Font("Rockwell", 1.15 * lineSize)
  val textFont = new Font("Rockwell", 0.75 * lineSize)

}
