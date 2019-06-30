package io.github.reggert.reb4j.test

import io.github.reggert.reb4j.{Expression, Group, Quantified, UnboundedLookBehindException}
import org.junit.Test
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.scalacheck.{Gen, Prop}
import org.scalatestplus.junit.JUnitSuite
import org.scalatestplus.scalacheck.Checkers


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
					for {
						n <- arbitrary[Int] if n > 0
						quantifiable <- quantifiableGen
					} yield quantifiable.atLeast(n, mode)
				)
		} yield quantified
	}
	
	def unboundedLookBehindException: Prop = forAll(Gen.sized(n => genIndefinite(n + 1))){ nested =>
		Prop.throws(classOf[UnboundedLookBehindException])(constructor(nested))
	}
}

class UnboundedPositiveLookBehindProps extends LookBehindProps("UnboundedPositiveLookBehind")(Group.positiveLookBehind)
{
	@Test def testUnboundedLookBehind() : Unit = check(unboundedLookBehindException, minSuccessful(100000), maxDiscardedFactor(5.0))
}

class UnboundedNegativeLookBehindProps extends LookBehindProps("UnboundedNegativeLookBehind")(Group.negativeLookBehind)
{
	@Test def testUnboundedLookBehind() : Unit = check(unboundedLookBehindException, minSuccessful(100000), maxDiscardedFactor(5.0))
}



