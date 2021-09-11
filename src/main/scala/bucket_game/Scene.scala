package bucket_game

import bucket_game.components.Component
import bucket_game.domain.Body
import bucket_game.lib.vecmath.Vect2

class Scene(
             val components: List[Component[_ <: Body]] = Nil,
             var gravity: Vect2 = Vect2(0, -10f),
             var dt: Float = 0.01f
           )
