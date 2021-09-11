package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

class Collision(
                 val firstBody: Body,
                 val secondBody: Body,
                 val normal: Vect2
               )

object Collision {
  private[domain] class CollisionFactory(val normal: Vect2) {
    def apply(firstBody: Body, secondBody: Body): Collision = {
      new Collision(firstBody, secondBody, normal)
    }
  }

  private[domain] def buildCollision(normal: Vect2) = new CollisionFactory(normal)
}
