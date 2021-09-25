package bucket_game.ui.panels

import bucket_game.game_management.Scene
import bucket_game.ui.{ConsoleWindowAPI, VirtualView}

class CommonPanel(
                   val positionX: Int = 0,
                   val positionY: Int = 0,
                   val width: Int,
                   val height: Int
                 ) extends Panel with VirtualView {
  def render(consoleWindowAPI: ConsoleWindowAPI, scene: Scene): Unit = {
    for {
      y <- matrix.indices
      (char, x) <- matrix(y).zipWithIndex
    } consoleWindowAPI.setPixel(positionX + x, positionY + y, char)
  }
}
