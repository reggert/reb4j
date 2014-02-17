package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4j.Expression
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test



class ExpressionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Expression] 
	with ExpressionGenerators with ExpressionShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[Expression])
}


