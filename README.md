# ScalaSnake
Simple Snake game written in Scala with ScalaFX and Akka Actors.

## Goal

This task was part of university course assignment to create application using functional apporach with as less variables as possible.

The overall desing is based on trait `State` represented by multiple different case classes. Those classes represent the overall state of the game and holds vital information. Logic is then defined as simple side effect free transformations from current state to next state.

State is stored in the closure of lambda functions used in Akka Actor. There are two different types of actor:
1) `Root` - Accepts multiple different messages - `TickStarted`, `TickFinished`, `OnKeyPress`
   1) If message `TickStarted` is received then first child actor is notified with the current state of the game stored in the closure to begin the transformation chain. The root behavior is also changed changed to expect `TickFinished` from last child, which holds the new recalculated state.
   2) If message `OnKeyPress` is received then no child is notified, only the behavior of root actor is changed to reflect new direction of snake - closure is changed.
   3) If message `TickFinished` is received then closure is updated with whatever the state is received from the message. It is sent only by child to root.
2) `Child` - Apply single side effect free transformation to the current state and informs next child actor. (Last child informs Root with `TickFinished`)

## Configuration

For quick and simple configuration `scopt` library was used to parse arguments passed on program startup.

| Shortcut | Command | Default | Description                                     |
|----------|---------|---------|-------------------------------------------------|
| `-w`        | `--width`   | 31      | Number of cells in row                          |
| `-h`        | `--height`  | 31      | Number of cells in collumn                      |
| `-s`        | `--scale`   | 16      | Number of pixels representing width of one cell |
|          | `--wall`    | 20      | Maximum number of walls at one time             |
|          | `--food`    | 3       | Maximum number of food at one time              |
| `-t`        | `--time`    | 120     | Logic refresh rate in ms                        |

## Running

Since JavaFX is no longer bundled together with Java JDK either the JavaFX SDK must be installed in the system or fat JAR withto correct platform must be compilled.

Minimum required version of Java is 17+

To build the application run following:

`./gradlew clean build`

To run the application from command line, use following:

`java -jar build/libs/ScalaSnake-2.13.9-all.jar`

## Samples

Following imagas captures all game states - Before first game, during the game and after losing.

![Before game](/assets/images/begin.png)
![During game](/assets/images/progress.png)
![After loosing](/assets/images/finish.png)