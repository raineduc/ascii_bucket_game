package bucket_game.renderers

import bucket_game.components.Renderer
import bucket_game.domain.Ball
import bucket_game.ConsoleRenderAPI
import bucket_game.lib.vecmath.Vect2

class BallRenderer(
                  private val renderAPI: ConsoleRenderAPI
                  ) extends Renderer[Ball] {
  private val texture =
    """
    |  xx
    | x  x
    |x    x
    |x    x
    | x  x
    |  xx
    """.stripMargin

  private val splittedTexture = texture.split("(\r\n)|\n").map(line => line.zipWithIndex).zipWithIndex

  def render(body: Ball): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(body.position)

    for {
      (line, heightOffset) <- splittedTexture
      if (top + heightOffset) <= renderAPI.height
      (char, widthOffset) <- line
      if (left + widthOffset) <= renderAPI.width && char != ' '
    } {
      renderAPI.setPixel(left + widthOffset, top + heightOffset, char)
    }
  }
}

