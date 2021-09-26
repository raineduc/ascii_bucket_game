package bucket_game.ui.panels

import bucket_game.game_management.Scene
import bucket_game.ui.ConsoleWindowAPI
import bucket_game.lib.vecmath.polarAngle
import bucket_game.lib.string_utils.fillWithSpaces
import scala.math.toDegrees

class InfoCommonPanel(
                 positionX: Int = 0,
                 positionY: Int = 0,
                 width: Int,
                 height: Int
               ) extends CommonPanel(positionX, positionY, width, height) {
  private def panelString(velocity: Double, angle: Double): String =
    s"${"-" * width}\n" +
      fillWithSpaces(width) {
        f"|ball velocity: $velocity%.2f[]|\n"
      } +
      fillWithSpaces(width) {
        f"|ball direction degree: ${toDegrees(angle)}%.2f[]|\n"
      } +
      fillWithSpaces(width)("|[]|\n") * (height - 4) +
      s"${"-" * width}\n"


  private def fillMatrix(scene: Scene): Unit = {
    val ball = scene.ball
    val lines = panelString(ball.velocity.getModule, polarAngle(ball.velocity)).split("\n")
    for {
      (line, y) <- lines.zipWithIndex
      (char, x) <- line.zipWithIndex
    } setPixel(x, y, char)
  }

  override def render(consoleWindowAPI: ConsoleWindowAPI, scene: Scene): Unit = {
    fillMatrix(scene)
    super.render(consoleWindowAPI, scene)
  }
}
