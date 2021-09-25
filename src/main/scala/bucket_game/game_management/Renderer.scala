package bucket_game.game_management

import bucket_game.domain.Body

trait Renderer[T <: Body] {
  def render(body: T): Unit
}
