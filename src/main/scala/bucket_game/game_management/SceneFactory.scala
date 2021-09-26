package bucket_game.game_management


/*
* Scene objects are to complicated to deep clone, therefore
* GameManager requires Scene factories to create unique scenes with the same initial state
* */
trait SceneFactory {
  def scene(): Scene
}
