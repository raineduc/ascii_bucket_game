package bucket_game.ui.renderers

import bucket_game.domain.Bucket
import bucket_game.game_management.Renderer
import bucket_game.ui.{Panel, RenderAPIImpl}

class BucketRenderer(
                      renderAPI: RenderAPIImpl,
                      gamePanel: Panel
                    ) extends Renderer[Bucket] {
  /*
    -------------
    \           /
     -----------
  */

  def render(bucket: Bucket): Unit = {
    val (left, top) = gamePanel.cartesianToPanelCoordinates(bucket.center)
    val radius = bucket.radius.round

    gamePanel.setPixel(left - radius, top, '\\')
    gamePanel.setPixel(left + radius, top, '/')

    for (x <- (left - radius) to (left + radius)) {
      gamePanel.setPixel(x, top - 1, '-')
      if (x > left - radius && x < left + radius) {
        gamePanel.setPixel(x, top + 1, '-')
      }
    }
  }
}