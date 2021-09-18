package bucket_game.game_management

import bucket_game.domain.{Body, Collision, InfiniteMass, Shape}
import bucket_game.lib.vecmath.Vect2

import scala.math.{abs, min}

class PhysicsAPI {
  private val correctionPercent = 0.8f
  private val correctionSlop = 0.01f

  private def invertMass(body: Body): Float = if (body.mass == InfiniteMass) 0 else 1 / body.mass

//  private def correctPosition(collision: Collision): Unit = {
//    val (body1, body2) = (collision.firstBody, collision.secondBody)
//
//    val invertedBody1Mass = invertMass(body1)
//    val invertedBody2Mass = invertMass(body2)
//
//    if (invertedBody1Mass == 0 && invertedBody2Mass == 0) return
//
//    val correction = collision.normal *
//      (max(collision.penetration - correctionSlop, 0f) / (invertedBody1Mass + invertedBody2Mass) * correctionPercent)
//
//    body1.position -= correction * invertedBody1Mass
//    body2.position += correction * invertedBody2Mass
//  }


  // subtracts from gravity the normal force projection onto it
  private def correctPosition(scene: Scene, collision: Collision): Unit = {
    val (body1, body2) = (collision.firstBody, collision.secondBody)

    val cosBetween = scene.gravity.cosBetween(collision.normal)
    val normalForceAcceleration = scene.gravity.getModule * abs(cosBetween)
    val scaledNormal = collision.normal * normalForceAcceleration
    val gravityScale = scaledNormal.getModule * cosBetween
    val normalForceAccelerationProjection = scene.gravity.normalize.getOrElse(Vect2(0, 0)) * gravityScale // vector projection

    if (body1.mass != InfiniteMass)
      body1.velocity -= normalForceAccelerationProjection * scene.dt
    if (body2.mass != InfiniteMass)
      body2.velocity += normalForceAccelerationProjection * scene.dt
  }

  def resolveCollision(collision: Collision): Unit = {
    val body1 = collision.firstBody
    val body2 = collision.secondBody

    val relativeVelocity = body2.velocity - body1.velocity
    val velocityAlongNormal = collision.normal dotProduct relativeVelocity

    if (velocityAlongNormal > 0) return // objects don't move towards each other

    val elasticity = min(body1.restitution, body2.restitution)

    val invertedBody1Mass = invertMass(body1)
    val invertedBody2Mass = invertMass(body2)

    if (invertedBody1Mass == 0 && invertedBody2Mass == 0) return

    val impulseOfForce = (-(1 + elasticity) * velocityAlongNormal) / (invertedBody1Mass + invertedBody2Mass)

    val impulseVector = collision.normal * impulseOfForce
    body1.velocity -= impulseVector * invertedBody1Mass
    body2.velocity += impulseVector * invertedBody2Mass
  }

  def updatePhysics(scene: Scene): Unit = {
    for (component <- scene.components) {
      component.gameObject.position += component.gameObject.velocity * scene.dt
    }

    for {
      (component1, idX) <- scene.components.zipWithIndex
      (component2, idY) <- scene.components.zipWithIndex
      if idX < idY
    } {
      Shape.defineCollision(component1.gameObject, component2.gameObject) match {
        case Some(collision) => {
          resolveCollision(collision)
          correctPosition(scene, collision)
        }
        case _ =>
      }
    }

    for (component <- scene.components if component.gameObject.mass != InfiniteMass) {
      val gameObject = component.gameObject
      gameObject.velocity += scene.gravity * scene.dt
    }
  }
}
