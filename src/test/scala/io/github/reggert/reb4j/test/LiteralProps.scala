package io.github.reggert.reb4j.test

import org.junit.Test
import org.junit.runner.RunWith
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.junit.{JUnitRunner,JUnitSuite}
import org.scalatest.prop.Checkers
import io.github.reggert.reb4j.{CharLiteral,Literal,StringLiteral}

class LiteralProps extends JUnitSuite with Checkers
	with ExpressionProperties[Literal] 
	with LiteralGenerators with LiteralShrinkers
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Literal]), minSuccessful(100000), maxDiscarded(500000))
}

class CharLiteralProps extends JUnitSuite with Checkers
	with ExpressionProperties[CharLiteral] 
	with LiteralGenerators
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[CharLiteral]), minSuccessful(100000), maxDiscarded(500000))
}

class StringLiteralProps extends JUnitSuite with Checkers
	with ExpressionProperties[StringLiteral] 
	with LiteralGenerators with LiteralShrinkers
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[StringLiteral]), minSuccessful(100000), maxDiscarded(500000))
}
