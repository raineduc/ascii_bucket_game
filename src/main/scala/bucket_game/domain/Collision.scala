package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

case class Collision(
                 firstShape: Shape,
                 secondShape: Shape,
                 normal: Vect2,
                 penetration: Float
               )

object Collision {
  private[domain] class CollisionFactory(val normal: Vect2, val penetration: Float) {
    def apply(firstShape: Shape, secondShape: Shape): Collision = {
      new Collision(firstShape, secondShape, normal, penetration)
    }
  }

  private[domain] def buildCollision(normal: Vect2, penetration: Float) = new CollisionFactory(normal, penetration)

  private[domain] def reverseShapeOrder(collision: Collision): Collision = {
    new Collision(
      collision.secondShape,
      collision.firstShape,
      collision.normal * (-1),
      collision.penetration
    )
  }
}
