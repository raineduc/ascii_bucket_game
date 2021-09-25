package bucket_game.game_management

trait Controller {
  def consumeCommandIfExists(gameManager: GameManager): Unit
}
