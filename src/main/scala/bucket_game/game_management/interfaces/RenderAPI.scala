package bucket_game.game_management.interfaces

import bucket_game.game_management.Scene

trait RenderAPI {
  def renderScene(scene: Scene): Unit

  def roundFinished(scene: Scene, success: Boolean): Unit
}
