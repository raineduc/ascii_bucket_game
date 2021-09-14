package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

class Collision(
                 val firstBody: Body,
                 val secondBody: Body,
                 val normal: Vect2,
                 val penetration: Float
               )

object Collision {
  private[domain] class CollisionFactory(val normal: Vect2, val penetration: Float) {
    def apply(firstBody: Body, secondBody: Body): Collision = {
      new Collision(firstBody, secondBody, normal, penetration)
    }
  }

  private[domain] def buildCollision(normal: Vect2, penetration: Float) = new CollisionFactory(normal, penetration)
}
