package bucket_game

import java.time.Instant
import java.time.temporal.ChronoUnit
import bucket_game.components.Component
import bucket_game.domain.{Ball, Bucket, Wall}
import bucket_game.renderers.{BallRenderer, BucketRenderer, WallRenderer}
import bucket_game.lib.vecmath.Vect2

object Main extends App {
  val fps = 60
  val dt: Float = 1f / fps
  var accumulator: Double = 0

  val consoleRenderAPI = new ConsoleRenderAPI(100, 50)
  val physicsAPI = new PhysicsAPI()

  val wallRenderer = new WallRenderer(consoleRenderAPI)

  val components = List(
    new Component[Ball](new Ball(Vect2(20, 20), Vect2(8, 40)), new BallRenderer(consoleRenderAPI)),
    new Component[Bucket](new Bucket(Vect2(80, 30), 6), new BucketRenderer(consoleRenderAPI)),
    new Component[Wall](new Wall(Vect2(0, 49), 100, true), wallRenderer),
    new Component[Wall](new Wall(Vect2(99, 48), 49), wallRenderer),
    new Component[Wall](new Wall(Vect2(1, 0), 98, true), wallRenderer),
    new Component[Wall](new Wall(Vect2(0, 48), 48), wallRenderer)
  )

  val scene = new Scene(
    components = components,
    dt = dt
  )

  var frameStart = Instant.now()

  while (true) {
    val currentTime = Instant.now()
    accumulator += ChronoUnit.MILLIS.between(frameStart, currentTime) * 0.001

    frameStart = currentTime
    while (accumulator > dt) {
      physicsAPI.updatePhysics(scene)
      accumulator -= dt
    }
    consoleRenderAPI.renderScene(scene)
    Thread.sleep(50)
  }
}
