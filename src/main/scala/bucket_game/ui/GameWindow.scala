package bucket_game.ui

import bucket_game.ui.panels.Panel

class GameWindow(
                  val gamePanel: Panel,
                  val infoPanel: Panel
                ) {
  var panels = List(gamePanel, infoPanel)
}
