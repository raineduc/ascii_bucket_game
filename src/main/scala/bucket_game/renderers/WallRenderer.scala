package bucket_game.renderers

import bucket_game.ConsoleRenderAPI
import bucket_game.components.Renderer
import bucket_game.domain.Wall

class WallRenderer(
                    private val renderAPI: ConsoleRenderAPI
                  ) extends Renderer[Wall] {
  private def renderHorizontalWall(body: Wall): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(body.position)

    if (top >= 0 && top < renderAPI.height) {
      for {
        x <- left until (left + body.length).round.toInt
        if x >= 0 && x < renderAPI.width
      } {
        renderAPI.setPixel(x, top, '-');
      }
    }
  }

  private def renderVerticalWall(body: Wall): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(body.position)

    if (left >= 0 && left < renderAPI.width) {
      for {
        y <- top until (top + body.length).round.toInt
        if y >= 0 && y < renderAPI.height
      } {
        renderAPI.setPixel(left, y, '|');
      }
    }
  }

  def render(body: Wall): Unit = {
    if (body.isHorizontal) renderHorizontalWall(body) else renderVerticalWall(body)
  }
}
