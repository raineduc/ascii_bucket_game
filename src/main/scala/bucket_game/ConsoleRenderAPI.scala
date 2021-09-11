package bucket_game

import bucket_game.components.Component
import bucket_game.domain.Body
import bucket_game.lib.vecmath.Vect2

class ConsoleRenderAPI(
  val width: Int,
  val height: Int
) {
  val matrix: Array[Array[Char]] = Array.fill[Char](height, width)(' ')

  initTerminal()

  private def initTerminal(): Unit = {
//    print(f"\u001b[${height}T")
    print(f"\u001b[${height}S")
    print(s"\u001b[${height}A")
    print("\u001b[s")
  }

  private def clearTerminal(): Unit = {
    print("\u001b[u")
  }

  private def clearMatrix(): Unit = {
    for {
      y <- 0 until height
      x <- 0 until width
    } matrix(y)(x) = ' '
  }

  private def renderScreen(): Unit = {
    print(
      matrix.map(line => line.mkString("")).mkString("\n")
    )
  }

  def cartesianToConsole(vect: Vect2): (Int, Int) = {
    (vect.x.round.toInt, (height - vect.y - 1).round.toInt)
  }

  def renderScene(scene: Scene): Unit = {
    clear()
    for (component <- scene.components) {
      component.render()
    }
    renderScreen()
  }

  def setPixel(x: Int, y: Int, char: Char): Unit = {
    if (x >= 0 && x < width && y >= 0 && y < height) matrix(y)(x) = char
  }

  def clear(): Unit = {
    clearMatrix()
    clearTerminal()
  }
}
