package bucket_game.renderers

import bucket_game.ConsoleRenderAPI
import bucket_game.components.Renderer
import bucket_game.domain.Wall

class WallRenderer(
                    private val renderAPI: ConsoleRenderAPI
                  ) extends Renderer[Wall] {
  private def renderHorizontalWall(body: Wall): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(body.position)

    for {
      x <- left until (left + body.length).round
    } {
      renderAPI.setPixel(x, top, '-');
    }
  }

  private def renderVerticalWall(body: Wall): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(body.position)

    for {
      y <- top until (top + body.length).round
    } {
      renderAPI.setPixel(left, y, '|');
    }
  }

  def render(body: Wall): Unit = {
    if (body.isHorizontal) renderHorizontalWall(body) else renderVerticalWall(body)
  }
}
