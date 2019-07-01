package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.Expression
import org.junit.Test
import org.scalatestplus.junit.JUnitSuite
import org.scalatestplus.scalacheck.Checkers



class ExpressionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Expression] 
	with ExpressionGenerators with ExpressionShrinkers
{
	//noinspection AccessorLikeMethodIsUnit
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Expression]), minSuccessful(200000), maxDiscardedFactor(5.0))
}


