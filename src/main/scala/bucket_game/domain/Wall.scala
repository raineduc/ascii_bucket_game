package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

class Wall(
            position: Vect2,
            val length: Float,
            val isHorizontal: Boolean = false
          ) extends Body {
  var velocity: Vect2 = Vect2(0, 0)
  val shape: Shape = {
    if (isHorizontal) new AABBShape(position, Vect2(position.x + length - 1, position.y))
    else new AABBShape(position, Vect2(position.x, position.y - length + 1))
  }
  val density = 1f
  val mass: Float = InfiniteMass
  val restitution = 0.7f
}
