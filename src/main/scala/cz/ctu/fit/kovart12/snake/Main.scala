package cz.ctu.fit.kovart12.snake

import akka.actor.typed.ActorSystem
import cz.ctu.fit.kovart12.snake.actors.Root
import cz.ctu.fit.kovart12.snake.actors.Root.{InitializeState, OnKeyPress, Protocol, TickStarted}
import cz.ctu.fit.kovart12.snake.config.{Config, ConfigParser}
import cz.ctu.fit.kovart12.snake.state.{GameBeforeState, State}
import cz.ctu.fit.kovart12.snake.util.{Down, Orientation, Renderer, Up}
import scalafx.Includes._
import scalafx.application.{JFXApp3, Platform}
import scalafx.geometry.VPos
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.paint.Color._
import scalafx.scene.text.TextAlignment
import scopt.OParser

import scala.concurrent.duration.FiniteDuration
import scala.jdk.DurationConverters._

object Main extends JFXApp3 {

  var actorSystem : ActorSystem[Protocol] = null

  override def stopApp(): Unit = {
    actorSystem.terminate()
  }

  override def start(): Unit = {
    val config : Config = OParser.parse(ConfigParser.parser, parameters.raw, Config()) match {
      case Some(config) =>
        println(s"Game configuration: ${config.toString}")
        config
      case _ =>
        Platform.exit()
        System.exit(1)
        Config()
    }

    val canvas = new Canvas() {
      width = config.scale * config.boardWidth
      height = config.scale * config.boardHeight
    }
    canvas.graphicsContext2D.textAlign = TextAlignment.Center
    canvas.graphicsContext2D.textBaseline = VPos.Center

    val transformationChain : List[State => State] = List(move, createFood, checkBounds, checkCollisions, new Renderer(canvas.graphicsContext2D))
    actorSystem = ActorSystem(Root(transformationChain), "root")
    actorSystem.tell(InitializeState(GameBeforeState(config)))

    stage = new JFXApp3.PrimaryStage {
      title = "NI-PSL - Snake"
      scene = new Scene {
        fill = White
        content = canvas
        onKeyPressed = (key: KeyEvent) => {
          (key.code match {
            case KeyCode.Up | KeyCode.W => Option.apply(Up())
            case KeyCode.Down | KeyCode.S => Option.apply(Down())
            case KeyCode.Left | KeyCode.A => Option.apply(util.Left())
            case KeyCode.Right | KeyCode.D => Option.apply(util.Right())
            case _ => Option.empty[Orientation]
          }).map(orientation => actorSystem.tell(OnKeyPress(orientation)))
        }
      }
      resizable = false
    }
    actorSystem.scheduler.scheduleAtFixedRate(
      FiniteDuration.apply(config.time._1, config.time._2).toJava,
      FiniteDuration.apply(config.time._1, config.time._2).toJava,
      () => actorSystem.tell(TickStarted(null)),
      actorSystem.executionContext
    )
  }
}