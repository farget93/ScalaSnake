package cz.ctu.fit.kovart12.snake.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import cz.ctu.fit.kovart12.snake.actors.Root.{Protocol, TickFinished}
import cz.ctu.fit.kovart12.snake.state.State

object Child {

  def apply(
             transformation : State => State,
             nextNode : ActorRef[Protocol]
           ) : Behavior[Protocol] = Behaviors.receiveMessage {
    case TickFinished(state) =>
      nextNode.tell(TickFinished(transformation.apply(state)))
      Behaviors.same
  }

}
