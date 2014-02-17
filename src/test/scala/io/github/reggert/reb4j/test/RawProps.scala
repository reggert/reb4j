package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.Raw
import io.github.reggert.reb4j.Entity
import org.scalacheck.Test.Parameters.Default
import io.github.reggert.reb4j.Raw.EscapedLiteral
import org.scalatest.junit.JUnitSuite
import io.github.reggert.reb4j.Quantifiable
import org.scalatest.prop.Checkers
import org.junit.Test

class RawProps extends JUnitSuite with Checkers
	with ExpressionProperties[Raw] with RawGenerators with RawShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[Raw])
}


class CompoundRawProps extends JUnitSuite with Checkers
	with ExpressionProperties[Raw.Compound] with RawGenerators with RawShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[Raw.Compound])
}


class EntityProps extends JUnitSuite with Checkers
	with ExpressionProperties[Entity] with RawGenerators // not shrinkable
{
	@Test def toPattern : Unit = toPattern(arbitrary[Entity])
}


class EscapedLiteralProps extends JUnitSuite with Checkers
	with ExpressionProperties[EscapedLiteral] with RawGenerators with RawShrinkers
{
	@Test def toPattern : Unit = toPattern(arbitrary[EscapedLiteral])
}