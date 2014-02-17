package io.github.reggert.reb4j.test

import io.github.reggert.reb4j.charclass.CharClass
import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.charclass.SingleChar
import io.github.reggert.reb4j.charclass.MultiChar
import io.github.reggert.reb4j.charclass.CharRange
import io.github.reggert.reb4j.charclass.Intersection
import io.github.reggert.reb4j.charclass.Union
import io.github.reggert.reb4j.charclass.PredefinedClass
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test

class CharClassProps extends JUnitSuite with Checkers
	with ExpressionProperties[CharClass] 
	with CharClassProperties[CharClass]
	with CharClassGenerators
{
	@Test def toPattern : Unit = toPattern(arbitrary[CharClass])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[CharClass])
}


class SingleCharProps extends JUnitSuite with Checkers
	with ExpressionProperties[SingleChar] 
	with CharClassProperties[SingleChar]
	with CharClassGenerators
{
	@Test def toPattern : Unit = toPattern(arbitrary[SingleChar])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[SingleChar])
}


class MultiCharProps extends JUnitSuite with Checkers
	with ExpressionProperties[MultiChar]
	with CharClassProperties[MultiChar]
	with CharClassGenerators 
	with CharClassShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[MultiChar])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[MultiChar])
}


class CharRangeProps extends JUnitSuite with Checkers
	with ExpressionProperties[CharRange] 
	with CharClassProperties[CharRange]
	with CharClassGenerators
{
	@Test def toPattern : Unit = toPattern(arbitrary[CharRange])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[CharRange])
}


class IntersectionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Intersection]
	with CharClassProperties[Intersection]
	with CharClassGenerators
	with CharClassShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[Intersection])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[Intersection])
}


class UnionProps extends JUnitSuite with Checkers
	with ExpressionProperties[Union] 
	with CharClassProperties[Union]
	with CharClassGenerators
	with CharClassShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[Union])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[Union])
}


class PredefinedClassProps extends JUnitSuite with Checkers
	with ExpressionProperties[PredefinedClass]
	with CharClassProperties[PredefinedClass]
	with CharClassGenerators
{
	@Test def toPattern : Unit = toPattern(arbitrary[PredefinedClass])
	@Test def symmetricNegation : Unit = symmetricNegation(arbitrary[PredefinedClass])
}
