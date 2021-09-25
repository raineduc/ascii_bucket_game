package bucket_game.ui

import bucket_game.lib.vecmath.Vect2

class Panel(
             val positionX: Int,
             val positionY: Int,
             val width: Int,
             val height: Int
           ) extends VirtualView {
  def render(consoleWindowAPI: ConsoleWindowAPI): Unit = {
    for {
      y <- matrix.indices
      (char, x) <- matrix(y).zipWithIndex
    } consoleWindowAPI.setPixel(positionX + x, positionY + y, char)
  }

  def cartesianToPanelCoordinates(vect: Vect2): (Int, Int) = {
    (vect.x.round.toInt, (height - vect.y - 1).round.toInt)
  }
}
