package bucket_game.domain

import bucket_game.lib.vecmath.Vect2

class Collision(
                 val firstBody: Body,
                 val secondBody: Body,
                 val normal: Vect2
               )

object Collision {
  private[domain] type CollisionFactory = (Body, Body) => Collision

  private[domain] def buildCollision(normal: Vect2)(firstBody: Body, secondBody: Body): Collision = {
    new Collision(firstBody, secondBody, normal)
  }
}
