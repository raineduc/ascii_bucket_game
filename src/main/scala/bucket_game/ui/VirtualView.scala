package bucket_game.ui

trait VirtualView {
  val width: Int
  val height: Int
  lazy val matrix: Array[Array[Char]] = Array.fill[Char](height, width)(' ')

  def clearMatrix(): Unit = {
    for {
      y <- 0 until height
      x <- 0 until width
    } matrix(y)(x) = ' '
  }

  def setPixel(x: Int, y: Int, char: Char): Unit = {
    if (x >= 0 && x < width && y >= 0 && y < height) matrix(y)(x) = char
  }
}
