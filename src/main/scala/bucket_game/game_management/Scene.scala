package bucket_game.game_management

import bucket_game.domain.{Ball, Body, Bucket}
import bucket_game.lib.vecmath.Vect2

class Scene(
             val components: List[Component[_ <: Body]] = Nil,
             val ball: Ball,
             val bucket: Bucket,
             var gravity: Vect2 = Vect2(0, -10f),
             var dt: Float = 0.01f
           )
