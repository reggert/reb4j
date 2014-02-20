package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4j.{Expression,Group}
import Group.{PositiveLookBehind,NegativeLookBehind}
import org.scalacheck.{Gen,Prop}
import Prop.forAll
import scala.util.Try
import io.github.reggert.reb4j.UnboundedLookBehindException
import io.github.reggert.reb4j.Quantified
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test



sealed abstract class LookBehindProps[GroupType <: Group](name : String)(constructor : Expression => GroupType) 
	extends JUnitSuite with Checkers
	with ExpressionGenerators with ExpressionShrinkers
{
	def genIndefinite(size : Int) : Gen[Quantified] = {
		require(size > 0)
		for {
			mode <- Gen.oneOf(Quantified.Mode.GREEDY, Quantified.Mode.RELUCTANT, Quantified.Mode.POSSESSIVE)
			quantifiableGen = genQuantifiable(size)
			quantified <- Gen.oneOf(
					quantifiableGen map (_.anyTimes(mode)),
					quantifiableGen map (_.atLeastOnce(mode)),
					(for {
						n <- arbitrary[Int] if n > 0
						quantifiable <- quantifiableGen	
					} yield quantifiable.atLeast(n, mode))
				)
		} yield quantified
	}
	
	def unboundedLookBehindException = forAll(Gen.sized(n => genIndefinite(n + 1))){nested =>
		Prop.throws(classOf[UnboundedLookBehindException])(constructor(nested))
	}
}

class UnboundedPositiveLookBehindProps extends LookBehindProps("UnboundedPositiveLookBehind")(Group.positiveLookBehind)
{
	@Test def testUnboundedLookBehind = check(unboundedLookBehindException) 
}

class UnboundedNegativeLookBehindProps extends LookBehindProps("UnboundedNegativeLookBehind")(Group.negativeLookBehind)
{
	@Test def testUnboundedLookBehind = check(unboundedLookBehindException) 
}



