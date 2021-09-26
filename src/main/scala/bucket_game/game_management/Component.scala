package bucket_game.game_management

import bucket_game.domain.Body
import bucket_game.game_management.interfaces.Renderer

class Component[T <: Body](
                            val gameObject: T,
                            renderer: Renderer[T]
                          ) {
  def render(): Unit = renderer.render(gameObject)
}
