package bucket_game.game_management

import bucket_game.lib.vecmath.{Vect2, rotateVect2}

import java.time.Instant
import java.time.temporal.ChronoUnit

class GameManager(
                   val scene: Scene,
                   val renderAPI: RenderAPI
                 ) {
  private val physicsAPI = new PhysicsAPI()
  var state: GameState.Value = GameState.Pending

  def increaseBallVelocity(delta: Float): Unit = {
    val addedVector = scene.ball.velocity.normalize.getOrElse(Vect2(1, 0)) * delta
    val velocity = scene.ball.velocity
    state match {
      case GameState.Pending =>
        if (delta < 0 && addedVector.lengthSquared > velocity.lengthSquared)
          scene.ball.velocity = Vect2(0, 0)
        else scene.ball.velocity += addedVector
      case _ =>
    }
  }

  def decreaseBallVelocity(delta: Float): Unit = increaseBallVelocity(-delta)


  /*
  * angle is measured in radians
  * positive value - counterclockwise rotation
  * negative value - clockwise rotation
  * */
  def rotateBallDirection(deg: Float): Unit = {
    state match {
      case GameState.Pending => scene.ball.velocity = rotateVect2(scene.ball.velocity, deg)
      case _ =>
    }
  }

  def startRound(): Unit = {
    state = GameState.Running

    var accumulator: Double = 0

    var frameStart = Instant.now()
    while (true) {
      val currentTime = Instant.now()
      accumulator += ChronoUnit.MILLIS.between(frameStart, currentTime) * 0.001

      frameStart = currentTime
      while (accumulator > scene.dt) {
        physicsAPI.updatePhysics(scene)
        accumulator -= scene.dt
      }
      renderAPI.renderScene(scene)
      Thread.sleep(50)
    }
  }
}
