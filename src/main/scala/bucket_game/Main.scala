package bucket_game

import bucket_game.controller.UserInput
import bucket_game.domain.{Ball, Bucket, Wall}
import bucket_game.game_management.{Component, GameManager, GameState, PhysicsAPI, Scene}
import bucket_game.ui.renderers.{BallRenderer, BucketRenderer, WallRenderer}
import bucket_game.lib.vecmath.Vect2
import bucket_game.ui.{GameWindow, Panel, RenderAPIImpl}
import org.jline.terminal.TerminalBuilder

object Main extends App {
  val fps = 60
  val dt: Float = 1f / fps

  private val terminal = TerminalBuilder.builder()
    .jna(true)
//    .system(true)
    .build()
  terminal.enterRawMode()

  val gamePanel = new Panel(0, 10, 100, 50)
  val gameWindow = new GameWindow(
    gamePanel = gamePanel,
    infoPanel = new Panel(0, 0, 100, 10)
  )
  val renderAPI = new RenderAPIImpl(100, 60, gameWindow, terminal)
  val physicsAPI = new PhysicsAPI()

  val wallRenderer = new WallRenderer(renderAPI, gamePanel)


  val ballComponent = new Component[Ball](
    new Ball(Vect2(1, 0), Vect2(8, 40)),
    new BallRenderer(renderAPI, gamePanel)
  )

  val bucketComponent = new Component[Bucket](
    new Bucket(Vect2(80, 30), 6),
    new BucketRenderer(renderAPI, gamePanel)
  )

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

  val gameManager = new GameManager(scene, renderAPI)
  val userInput = new UserInput(gameManager, terminal = terminal)

  renderAPI.renderScene(scene)
  while (gameManager.state == GameState.Pending) {
    userInput.consumeCommandIfExists()
  }
}
