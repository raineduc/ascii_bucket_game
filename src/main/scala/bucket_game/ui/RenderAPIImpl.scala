package bucket_game.ui

import bucket_game.game_management.Scene
import bucket_game.game_management.interfaces.RenderAPI
import bucket_game.lib.vecmath.Vect2
import org.jline.terminal.Terminal

class RenderAPIImpl(
                     val width: Int,
                     val height: Int,
                     gameWindow: GameWindow,
                     terminal: Terminal
                   ) extends RenderAPI {
  private val windowAPI = new ConsoleWindowAPI(width, height, terminal)

  def renderScene(scene: Scene): Unit = {
    windowAPI.clear()

    for (panel <- gameWindow.panels) panel.clear()

    for (component <- scene.components) {
      component.render()
    }
    for (panel <- gameWindow.panels) panel.render(windowAPI, scene)

    windowAPI.renderScreen()
  }

  def roundFinished(scene: Scene, success: Boolean): Unit = {
    windowAPI.clear()
    for (panel <- gameWindow.panels) panel.render(windowAPI, scene)
    windowAPI.renderScreen()
  }

  def cartesianToConsole(vect: Vect2): (Int, Int) = {
    (vect.x.round.toInt, (height - vect.y - 1).round.toInt)
  }
}
