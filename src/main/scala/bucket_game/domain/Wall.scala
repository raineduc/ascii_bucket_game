package bucket_game.domain

import bucket_game.vecmath.Vect2

class Wall(
            position: Vect2,
            val length: Float,
            val isHorizontal: Boolean = false
          ) extends Body {
  override var velocity: Vect2 = Vect2(0, 0)
  val shape: Shape = {
    if (isHorizontal) new AABBShape(position, position + Vect2(position.x + length, position.y))
    else new AABBShape(position, position + Vect2(position.x, position.y - length))
  }
  val density = 1f
  val mass = InfiniteMass
  val restitution = 0.7f
}
