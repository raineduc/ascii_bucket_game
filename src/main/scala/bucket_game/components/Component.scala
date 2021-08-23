package bucket_game.components

import bucket_game.domain.Body

abstract class Component[T <: Body] {
  val gameObject: T
  val renderer: Renderer[T]

  def render(): Unit = renderer.render(gameObject)
}
