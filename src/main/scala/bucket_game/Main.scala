package bucket_game

import java.time.Instant
import java.time.temporal.ChronoUnit
import bucket_game.components.BallComponent
import bucket_game.domain.Ball
import bucket_game.renderers.BallRenderer
import bucket_game.vecmath.Vect2

object Main extends App {
  val fps = 60
  val dt: Float = 1f / fps
  var accumulator: Double = 0

  val consoleRenderAPI = new ConsoleRenderAPI(100, 50)
  val physicsAPI = new PhysicsAPI()

  val components = List(
    new BallComponent(new Ball(Vect2(10, 10), 5, Vect2(8, 5)), new BallRenderer(consoleRenderAPI))
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
