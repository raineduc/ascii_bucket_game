package bucket_game.game_management

trait RenderAPI {
  def renderScene(scene: Scene): Unit
  def roundFinished(scene: Scene, success: Boolean): Unit
}
