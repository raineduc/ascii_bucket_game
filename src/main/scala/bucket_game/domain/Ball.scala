package bucket_game.domain

import bucket_game.vecmath.Vect2

class Ball(
            override var velocity: Vect2,
            val radius: Float,
            val centerPos: Vect2
          ) extends Body {
  override val shape: CircleShape = new CircleShape(centerPos, radius)
  val mass: Float = 5
  val restitution: Float = 1

  def this(radius: Float, centerPos: Vect2) = this(Vect2(0, 0), radius, centerPos)
}
