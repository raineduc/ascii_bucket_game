package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

abstract class Body {
  val shape: Shape
  val density: Float
  val mass: Float
  val restitution: Float
  var velocity: Vect2

  def position: Vect2 = shape.topLeft

  def position_=(pos: Vect2): Unit = {
    shape.changePosition(pos)
  }
}
