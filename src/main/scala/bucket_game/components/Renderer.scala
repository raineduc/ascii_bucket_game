package bucket_game.components

import bucket_game.domain.Body

trait Renderer[T <: Body] {
  def render(body: T): Unit
}
