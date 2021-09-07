package bucket_game.domain

import bucket_game.vecmath.Vect2

class Ball(
            override var velocity: Vect2,
            val radius: Float,
            val centerPos: Vect2
          ) extends Body {
  val shape: CircleShape = new CircleShape(centerPos, radius)
  val density: Float = 0.5f
  val mass: Float = shape.calculateMass(density)
  val restitution: Float = 1

  def this(radius: Float, centerPos: Vect2) = this(Vect2(0, 0), radius, centerPos)
}
