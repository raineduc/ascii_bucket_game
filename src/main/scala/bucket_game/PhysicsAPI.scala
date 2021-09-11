package bucket_game

import bucket_game.components.Component
import bucket_game.domain.{Body, Collision, InfiniteMass, Shape}
import bucket_game.lib.vecmath.Vect2

import math.min

class PhysicsAPI {
  def resolveCollision(collision: Collision): Unit = {
    val body1 = collision.firstBody
    val body2 = collision.secondBody

    val relativeVelocity = body2.velocity - body1.velocity
    val velocityAlongNormal = collision.normal dotProduct relativeVelocity

    if (velocityAlongNormal > 0) return // objects don't move towards each other

    val elasticity = min(body1.restitution, body2.restitution)

    val invertedBody1Mass = if (body1.mass == InfiniteMass) 0 else 1 / body1.mass
    val invertedBody2Mass = if (body2.mass == InfiniteMass) 0 else 1 / body2.mass

    if (invertedBody1Mass == 0 && invertedBody2Mass == 0) return

    val impulseOfForce = (-(1 + elasticity) * velocityAlongNormal) / (invertedBody1Mass + invertedBody2Mass)

    val impulseVector = collision.normal * impulseOfForce
    body1.velocity -= impulseVector * invertedBody1Mass
    body2.velocity += impulseVector * invertedBody2Mass
  }

  def updatePhysics(elems: List[Component[_ <: Body]], dt: Float): Unit = {
    for (component <- elems) {
      component.gameObject.position += component.gameObject.velocity * dt
    }

    for {
      (component1, idX) <- elems.zipWithIndex
      (component2, idY) <- elems.zipWithIndex
      if idX < idY
    } {
      Shape.defineCollision(component1.gameObject, component2.gameObject) match {
        case Some(collision) => resolveCollision(collision)
        case _ =>
      }
    }
  }
}
