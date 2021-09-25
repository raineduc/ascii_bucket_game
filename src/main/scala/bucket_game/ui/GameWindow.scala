package bucket_game.ui

import bucket_game.ui.panels.CommonPanel

class GameWindow(
                  val gamePanel: CommonPanel,
                  val infoPanel: CommonPanel
                ) {
  val panels = List(gamePanel, infoPanel)
}
