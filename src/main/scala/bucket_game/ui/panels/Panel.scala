package bucket_game.ui.panels

import bucket_game.game_management.Scene
import bucket_game.lib.vecmath.Vect2
import bucket_game.ui.ConsoleWindowAPI

trait Panel {
  val positionX: Int
  val positionY: Int
  val width: Int
  val height: Int

  def render(consoleWindowAPI: ConsoleWindowAPI, scene: Scene): Unit

  def cartesianToPanelCoordinates(vect: Vect2): (Int, Int) = {
    (vect.x.round.toInt, (height - vect.y - 1).round.toInt)
  }
}
