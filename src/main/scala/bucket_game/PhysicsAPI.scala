package bucket_game

import bucket_game.components.Component
import bucket_game.domain.Body

class PhysicsAPI {
  def updatePhysics(elems: List[Component[_ <: Body]], dt: Float): Unit = {
    for (component <- elems) {
      component.gameObject.position += component.gameObject.velocity * dt
    }
  }
}
