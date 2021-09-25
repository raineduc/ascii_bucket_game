package bucket_game.ui.renderers

import bucket_game.domain.Ball
import bucket_game.game_management.Renderer
import bucket_game.ui.RenderAPIImpl
import bucket_game.ui.panels.CommonPanel

class BallRenderer(
                    renderAPI: RenderAPIImpl,
                    gamePanel: CommonPanel
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
    val (left, top) = gamePanel.cartesianToPanelCoordinates(body.position)

    for {
      (line, heightOffset) <- splittedTexture
      if (top + heightOffset) <= renderAPI.height
      (char, widthOffset) <- line
      if (left + widthOffset) <= renderAPI.width && char != ' '
    } {
      gamePanel.setPixel(left + widthOffset, top + heightOffset, char)
    }
  }
}

