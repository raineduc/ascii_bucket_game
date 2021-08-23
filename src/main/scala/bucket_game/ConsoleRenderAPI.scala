package bucket_game

import bucket_game.components.Component

class ConsoleRenderAPI(
  private val width: Int,
  private val height: Int
) {
  private val matrix: Array[Array[Char]] = {
    (for (_ <- 0 until height) yield {
      new Array[Char](width)
    }).toArray
  }

  def renderScene(elems: List[Component[_]]): Unit = {
    for (body <- elems) {
      body.render()
    }
  }
}
