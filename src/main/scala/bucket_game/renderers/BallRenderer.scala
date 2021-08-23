package bucket_game.renderers

import bucket_game.components.Renderer
import bucket_game.domain.Ball
import bucket_game.ConsoleRenderAPI

class BallRenderer(
                  private val renderAPI: ConsoleRenderAPI
                  ) extends Renderer[Ball] {
  def render(body: Ball): Unit = {

  }
}
