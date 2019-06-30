package io.github.reggert.reb4j.test

import io.github.reggert.reb4j.Expression
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Prop, Shrink}


trait ExpressionProperties[E <: Expression] {
	def toPattern(g : Gen[E])(implicit s : Shrink[E]): Prop =
		forAll(g) {e : E => e.toPattern; true}
}