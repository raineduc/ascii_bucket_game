package bucket_game.renderers

import bucket_game.components.Renderer
import bucket_game.domain.Ball
import bucket_game.ConsoleRenderAPI
import bucket_game.vecmath.Vect2

class BallRenderer(
                  private val renderAPI: ConsoleRenderAPI
                  ) extends Renderer[Ball] {
  private val texture =
    """
    |    x  x
    | x        x
    |x          x
    |x          x
    | x        x
    |    x  x
    """.stripMargin

  private val splittedTexture = texture.split("(\r\n)|\n").map(line => line.zipWithIndex).zipWithIndex

  def render(body: Ball): Unit = {
    val Vect2(left, top) = body.shape.topLeft

    for {
      (line, heightOffset) <- splittedTexture
      if (top + heightOffset) <= renderAPI.height
      (char, widthOffset) <- line
      if (left + widthOffset) <= renderAPI.width && char != ' '
    } {
      renderAPI.setPixel((left + widthOffset).toInt, (top + heightOffset).toInt, char)
    }
  }
}

