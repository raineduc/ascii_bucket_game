package bucket_game

import java.time.Instant
import java.time.temporal.ChronoUnit
import bucket_game.components.Component
import bucket_game.domain.{Ball, Wall}
import bucket_game.renderers.{BallRenderer, WallRenderer}
import bucket_game.vecmath.Vect2

object Main extends App {
  val fps = 60
  val dt: Float = 1f / fps
  var accumulator: Double = 0

  val consoleRenderAPI = new ConsoleRenderAPI(100, 50)
  val physicsAPI = new PhysicsAPI()

  val wallRenderer = new WallRenderer(consoleRenderAPI)

  val components = List(
    new Component[Ball](new Ball(Vect2(20, -20), 5, Vect2(8, 45)), new BallRenderer(consoleRenderAPI)),
    new Component[Wall](new Wall(Vect2(0, 49), 100, true), wallRenderer),
    new Component[Wall](new Wall(Vect2(99, 48), 50), wallRenderer)
  )

  var frameStart = Instant.now()

  while (true) {
    val currentTime = Instant.now()
    accumulator += ChronoUnit.MILLIS.between(frameStart, currentTime) * 0.001

    frameStart = currentTime
    while (accumulator > dt) {
      physicsAPI.updatePhysics(components, dt)
      accumulator -= dt
    }
    consoleRenderAPI.renderScene(components)
    Thread.sleep(50)
  }
}
