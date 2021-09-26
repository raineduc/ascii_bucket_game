package bucket_game.ui.panels

import bucket_game.game_management.{GameState, RoundFinished, Scene}
import bucket_game.lib.string_utils.fillWithSpaces
import bucket_game.ui.ConsoleWindowAPI


class RoundResultPanel(
                        positionX: Int,
                        positionY: Int,
                        width: Int,
                        height: Int
                      ) extends CommonPanel(positionX, positionY, width, height) {
  private def panelString(success: Boolean): String =
    s"${"-" * width}\n" +
      fillWithSpaces(width)("|[]|\n") +
      fillWithSpaces(width) {
        s"|[]${if (success) "success" else "failure"}[]|\n"
      } +
      fillWithSpaces(width)( "|[]|\n") +
      s"${"-" * width}\n"

  private def fillMatrix(success: Boolean): Unit = {
    val lines = panelString(success).split("\n")
    for {
      (line, y) <- lines.zipWithIndex
      (char, x) <- line.zipWithIndex
    } setPixel(x, y, char)
  }

  def this(screenWidth: Int, screenHeight: Int) = {
    this(
      (screenWidth - RoundResultPanel.panelWidth) / 2,
      (screenHeight - RoundResultPanel.panelHeight) / 2,
      RoundResultPanel.panelWidth,
      RoundResultPanel.panelHeight
    )
  }

  override def render(consoleWindowAPI: ConsoleWindowAPI, scene: Scene): Unit = {
    scene.state match {
      case RoundFinished(success) =>
        fillMatrix(success)
        super.render(consoleWindowAPI, scene)
      case _ =>
    }
  }
}

object RoundResultPanel {
  private val panelWidth = 15
  private val panelHeight = 5
}
