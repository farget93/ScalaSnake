package cz.ctu.fit.kovart12.snake.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import cz.ctu.fit.kovart12.snake.keyListener
import cz.ctu.fit.kovart12.snake.state.State
import cz.ctu.fit.kovart12.snake.util.Orientation

import scala.util.Random

object Root {

  trait Protocol{}

  case class InitializeState(state : State) extends Protocol
  case class TickStarted(state: State) extends Protocol
  case class TickFinished(state: State) extends Protocol
  case class OnKeyPress(orientation: Orientation) extends Protocol

  val keyPressed : (State, Orientation, ActorRef[Protocol]) => Behavior[Protocol] = (state, orientation, firstNode) => {
    Behaviors.receiveMessage {
      case OnKeyPress(orientation) => keyPressed(state, orientation, firstNode)
      case TickStarted(_) =>
        firstNode.tell(TickFinished(keyListener.apply(state, orientation)))
        Behaviors.receiveMessage {
          case TickFinished(state) => noKeyPressed(state, firstNode)
          case _ => Behaviors.same
        }
    }
  }

  val noKeyPressed : (State, ActorRef[Protocol]) => Behavior[Protocol] = (state, firstNode) => {
    Behaviors.receiveMessage {
      case OnKeyPress(orientation) => keyPressed(state, orientation, firstNode)
      case TickStarted(_) =>
        firstNode.tell(TickFinished(state))
        Behaviors.receiveMessage {
          case TickFinished(state) => noKeyPressed(state, firstNode)
          case _ => Behaviors.same
        }
    }
  }

  def apply(transformations : List[State => State]) : Behavior[Protocol] = Behaviors.setup {
    context => {
      // Map list of transformations from end to start to Child with reference to next Actor
      // Last node is sending messages back to this
      val firstNode = transformations
        .foldRight(context.self)((a, b) => context.spawn(Child.apply(a, b), Random.nextInt(10000).toString))
      Behaviors.receiveMessage {
        case InitializeState(state) => noKeyPressed(state, firstNode)
        case _ => Behaviors.same
      }
    }
  }
}
