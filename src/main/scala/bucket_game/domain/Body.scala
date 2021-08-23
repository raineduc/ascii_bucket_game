package bucket_game.domain

import bucket_game.vecmath.Vect2

abstract class Body {
  val shape: Shape
  val mass: Float
  val restitution: Float
  var velocity: Vect2
}
