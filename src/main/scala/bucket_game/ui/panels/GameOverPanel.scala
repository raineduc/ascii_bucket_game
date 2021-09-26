package bucket_game.ui.panels

import bucket_game.game_management.{GameOver, RoundFinished, Scene}
import bucket_game.lib.string_utils.fillWithSpaces
import bucket_game.ui.ConsoleWindowAPI

class GameOverPanel(
                        positionX: Int,
                        positionY: Int,
                        width: Int,
                        height: Int
                      ) extends CommonPanel(positionX, positionY, width, height) {
  private val panelString: String =
    s"${"-" * width}\n" +
      fillWithSpaces(width)("|[]|\n") +
      fillWithSpaces(width) {
        s"|[]game over[]|\n"
      } +
      fillWithSpaces(width)( "|[]|\n") +
      s"${"-" * width}\n"

  private def fillMatrix(): Unit = {
    val lines = panelString.split("\n")
    for {
      (line, y) <- lines.zipWithIndex
      (char, x) <- line.zipWithIndex
    } setPixel(x, y, char)
  }

  def this(screenWidth: Int, screenHeight: Int) = {
    this(
      (screenWidth - GameOverPanel.panelWidth) / 2,
      (screenHeight - GameOverPanel.panelHeight) / 2,
      GameOverPanel.panelWidth,
      GameOverPanel.panelHeight
    )
  }

  override def render(consoleWindowAPI: ConsoleWindowAPI, scene: Scene): Unit = {
    scene.state match {
      case GameOver() =>
        fillMatrix()
        super.render(consoleWindowAPI, scene)
      case _ =>
    }
  }
}

object GameOverPanel {
  private val panelWidth = 15
  private val panelHeight = 5
}
