package io.github.reggert.reb4j.test

import io.github.reggert.reb4j.charclass.CharClass
import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.charclass.SingleChar
import io.github.reggert.reb4j.charclass.MultiChar
import io.github.reggert.reb4j.charclass.CharRange
import io.github.reggert.reb4j.charclass.Intersection
import io.github.reggert.reb4j.charclass.Union
import io.github.reggert.reb4j.charclass.PredefinedClass
import org.junit.Test
import org.scalatestplus.junit.JUnitSuite
import org.scalatestplus.scalacheck.Checkers


//noinspection AccessorLikeMethodIsUnit
class CharClassProps extends JUnitSuite with Checkers
	with ExpressionProperties[CharClass] 
	with CharClassProperties[CharClass]
	with CharClassGenerators
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[CharClass]), minSuccessful(100000), maxDiscardedFactor(1.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[CharClass]))
}


//noinspection AccessorLikeMethodIsUnit
class SingleCharProps extends JUnitSuite with Checkers
	with ExpressionProperties[SingleChar] 
	with CharClassProperties[SingleChar]
	with CharClassGenerators
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[SingleChar]), minSuccessful(100000), maxDiscardedFactor(1.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[SingleChar]))
}


//noinspection AccessorLikeMethodIsUnit
class MultiCharProps extends JUnitSuite with Checkers
	with ExpressionProperties[MultiChar]
	with CharClassProperties[MultiChar]
	with CharClassGenerators 
	with CharClassShrinkers
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[MultiChar]), minSuccessful(100000), maxDiscardedFactor(1.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[MultiChar]))
}


//noinspection AccessorLikeMethodIsUnit
class CharRangeProps extends JUnitSuite with Checkers
	with ExpressionProperties[CharRange] 
	with CharClassProperties[CharRange]
	with CharClassGenerators
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[CharRange]), minSuccessful(100000), maxDiscardedFactor(5.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[CharRange]))
}


//noinspection AccessorLikeMethodIsUnit
class IntersectionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Intersection]
	with CharClassProperties[Intersection]
	with CharClassGenerators
	with CharClassShrinkers
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Intersection]), minSuccessful(100000), maxDiscardedFactor(5.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[Intersection]))
}


//noinspection AccessorLikeMethodIsUnit
class UnionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Union] 
	with CharClassProperties[Union]
	with CharClassGenerators
	with CharClassShrinkers
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Union]), minSuccessful(100000), maxDiscardedFactor(5.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[Union]))
}


//noinspection AccessorLikeMethodIsUnit
class PredefinedClassProps extends JUnitSuite with Checkers
	with ExpressionProperties[PredefinedClass]
	with CharClassProperties[PredefinedClass]
	with CharClassGenerators
{
	@Test def toPattern() : Unit = check(toPattern(arbitrary[PredefinedClass]), minSuccessful(100000), maxDiscardedFactor(5.0))
	@Test def symmetricNegation() : Unit = check(symmetricNegation(arbitrary[PredefinedClass]))
}
