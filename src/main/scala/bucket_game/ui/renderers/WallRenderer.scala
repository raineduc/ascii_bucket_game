package bucket_game.ui.renderers

import bucket_game.domain.Wall
import bucket_game.game_management.interfaces.Renderer
import bucket_game.ui.RenderAPIImpl
import bucket_game.ui.panels.CommonPanel

class WallRenderer(
                    renderAPI: RenderAPIImpl,
                    gamePanel: CommonPanel
                  ) extends Renderer[Wall] {
  private def renderHorizontalWall(body: Wall): Unit = {
    val (left, top) = gamePanel.cartesianToPanelCoordinates(body.position)

    for {
      x <- left until (left + body.length).round
    } {
      gamePanel.setPixel(x, top, '-');
    }
  }

  private def renderVerticalWall(body: Wall): Unit = {
    val (left, top) = gamePanel.cartesianToPanelCoordinates(body.position)

    for {
      y <- top until (top + body.length).round
    } {
      gamePanel.setPixel(left, y, '|');
    }
  }

  def render(body: Wall): Unit = {
    if (body.isHorizontal) renderHorizontalWall(body) else renderVerticalWall(body)
  }
}
