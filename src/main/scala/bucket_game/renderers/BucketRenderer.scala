package bucket_game.renderers

import bucket_game.ConsoleRenderAPI
import bucket_game.components.Renderer
import bucket_game.domain.Bucket

class BucketRenderer(
                      renderAPI: ConsoleRenderAPI
                    ) extends Renderer[Bucket] {
  /*
    -------------
    \           /
     -----------
  */

  def render(bucket: Bucket): Unit = {
    val (left, top) = renderAPI.cartesianToConsole(bucket.center)
    val radius = bucket.radius.round.toInt

    renderAPI.setPixel(left - radius, top, '\\')
    renderAPI.setPixel(left + radius, top, '/')

    for (x <- (left - radius) to (left + radius)) {
      renderAPI.setPixel(x, top - 1, '-')
      if (x > left - radius && x < left + radius) {
        renderAPI.setPixel(x, top + 1, '-')
      }
    }
  }
}
