package bucket_game.game_management.interfaces

import bucket_game.domain.Body

trait Renderer[T <: Body] {
  def render(body: T): Unit
}
