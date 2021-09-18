package bucket_game

import bucket_game.components.Component
import bucket_game.controller.UserInput
import bucket_game.domain.{Ball, Bucket, Wall}
import bucket_game.game_management.{GameManager, GameState, PhysicsAPI, Scene}
import bucket_game.renderers.{BallRenderer, BucketRenderer, WallRenderer}
import bucket_game.lib.vecmath.Vect2

object Main extends App {
  val fps = 60
  val dt: Float = 1f / fps

  val consoleRenderAPI = new ConsoleRenderAPI(100, 50)
  val physicsAPI = new PhysicsAPI()

  val wallRenderer = new WallRenderer(consoleRenderAPI)


  val ballComponent = new Component[Ball](new Ball(Vect2(1, 0), Vect2(8, 40)), new BallRenderer(consoleRenderAPI))
  val bucketComponent = new Component[Bucket](new Bucket(Vect2(80, 30), 6), new BucketRenderer(consoleRenderAPI))
  val components = List(
    ballComponent,
    bucketComponent,
    new Component[Wall](new Wall(Vect2(0, 49), 100, true), wallRenderer),
    new Component[Wall](new Wall(Vect2(99, 48), 49), wallRenderer),
    new Component[Wall](new Wall(Vect2(1, 0), 98, true), wallRenderer),
    new Component[Wall](new Wall(Vect2(0, 48), 48), wallRenderer)
  )

  val scene = new Scene(
    components = components,
    ballComponent.gameObject,
    bucketComponent.gameObject,
    dt = dt
  )

  val gameManager = new GameManager(scene, consoleRenderAPI)
  val userInput = new UserInput(gameManager)

  consoleRenderAPI.renderScene(scene)
  while (gameManager.state == GameState.Pending) {
    userInput.consumeCommandIfExists()
  }
}
