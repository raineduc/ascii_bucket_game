package bucket_game.game_management

import bucket_game.domain.Shape
import bucket_game.lib.vecmath.{Vect2, rotateVect2}

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.{Timer, TimerTask}
import scala.math.signum

class GameManager(
                   val scene: Scene,
                   val renderAPI: RenderAPI,
                   val controller: Controller
                 ) {
  private val physicsAPI = new PhysicsAPI()
  private val PenetrationThreshold = scene.ball.radius / 2
  private var currentCollisionNormal: Option[Vect2] = None
  private var success = false
  private val VelocityThreshold = 0.1f
  private val TimeThreshold = 5
  private var countdownTimestamp: Option[Instant] = None

  scene.state = Pending()

  def state: GameState = scene.state

  private def completeRoundDelayed(delay: Long): Unit = {
    val timer = new Timer()
    val task = new TimerTask {
      def run(): Unit = {
        scene.state = RoundFinished(success)
        renderAPI.roundFinished(scene, success)
      }
    }
    timer.schedule(task, delay)
  }

  private def roundCompletionCheck(): Unit = {

    // velocity projection onto the gravity vector
    val velocityProjection = scene.ball.velocity.getModule * scene.ball.velocity.cosBetween(scene.gravity)

    if (velocityProjection < VelocityThreshold && !success) {
      countdownTimestamp match {
        case Some(ts) => {
          val secondsPassed = ChronoUnit.MILLIS.between(ts, Instant.now()) * 0.001
          if (secondsPassed > TimeThreshold) {
            scene.state = RoundFinished(success)
            renderAPI.roundFinished(scene, success)
          }
        }
        case None => countdownTimestamp = Some(Instant.now())
      }
    }
    else countdownTimestamp = None

    Shape.defineCollision(scene.ball.shape, scene.bucket.hitDetectionLayer) match {
      case Some(col) => {
        currentCollisionNormal match {
          case Some(_) =>
            if (signum(col.normal.y) > 0 && !success) { // direction changes from negative to positive, so the ball flies from top to bottom
              success = true
              completeRoundDelayed(1000)
            }
          case _ =>
            if (signum(col.normal.y) < 0 && col.penetration > PenetrationThreshold) {
              currentCollisionNormal = Some(col.normal)
            }
        }
      }
      case _ => currentCollisionNormal = None
    }
  }

  def increaseBallVelocity(delta: Float): Unit = {
    val addedVector = scene.ball.velocity.normalize.getOrElse(Vect2(1, 0)) * delta
    val velocity = scene.ball.velocity
    state match {
      case Pending() =>
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
      case Pending() => scene.ball.velocity = rotateVect2(scene.ball.velocity, deg)
      case _ =>
    }
  }

  def startAction(): Unit = {
    scene.state = Running()
  }

  def startRound(): Unit = {
    scene.state = Pending()
    startLoop()
  }

  def startLoop(): Unit = {
    var accumulator: Double = 0

    var frameStart = Instant.now()
    while (true) {
      val currentTime = Instant.now()
      accumulator += ChronoUnit.MILLIS.between(frameStart, currentTime) * 0.001

      frameStart = currentTime
      while (accumulator > scene.dt) {
        state match {
          case Running() =>
            physicsAPI.updatePhysics(scene)
            roundCompletionCheck()
          case _ =>
        }
        accumulator -= scene.dt
      }
      renderAPI.renderScene(scene)
      controller.consumeCommandIfExists(this)
      Thread.sleep(50)
    }
  }
}
