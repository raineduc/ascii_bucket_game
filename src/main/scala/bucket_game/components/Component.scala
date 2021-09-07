package bucket_game.components

import bucket_game.domain.Body

class Component[T <: Body](
                            val gameObject: T,
                            renderer: Renderer[T]
                          ) {
  def render(): Unit = renderer.render(gameObject)
}
