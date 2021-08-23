package bucket_game

import java.time.Instant
import java.time.temporal.ChronoUnit
import bucket_game.components.BallComponent
import bucket_game.domain.Ball
import bucket_game.renderers.BallRenderer
import bucket_game.vecmath.Vect2

object Main extends App {
  val fps = 100
  val dt = 1 / fps
  var accumulator: Float = 0

  val consoleRenderAPI = new ConsoleRenderAPI(600, 400)
  val components = List(
    new BallComponent(new Ball(5, Vect2(8, 5)), new BallRenderer(consoleRenderAPI))
  )

  var frameStart = Instant.now()

  while (true) {
    val currentTime = Instant.now()
    accumulator += ChronoUnit.SECONDS.between(currentTime, frameStart)

    frameStart = currentTime

    while (accumulator > dt) {

      accumulator -= dt
    }
    consoleRenderAPI.renderScene(components)
  }


  println("test")
}
