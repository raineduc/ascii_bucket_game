package bucket_game.game_management.interfaces

import bucket_game.game_management.GameManager

trait Controller {
  def consumeCommandIfExists(gameManager: GameManager): Unit
}
