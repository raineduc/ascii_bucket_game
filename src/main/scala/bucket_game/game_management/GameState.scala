package bucket_game.game_management

abstract class GameState

case class Pending() extends GameState

case class Running() extends GameState

case class RoundFinished(
                          success: Boolean
                        ) extends GameState
