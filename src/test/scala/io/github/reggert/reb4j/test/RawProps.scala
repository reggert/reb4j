package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.Raw
import io.github.reggert.reb4j.Entity
import io.github.reggert.reb4j.Raw.EscapedLiteral
import org.junit.Test
import org.scalatestplus.junit.JUnitSuite
import org.scalatestplus.scalacheck.Checkers


class RawProps extends JUnitSuite with Checkers
	with ExpressionProperties[Raw] with RawGenerators with RawShrinkers
{
	//noinspection AccessorLikeMethodIsUnit
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Raw]), minSuccessful(100000), maxDiscardedFactor(5.0))
}


class CompoundRawProps extends JUnitSuite with Checkers
	with ExpressionProperties[Raw.Compound] with RawGenerators with RawShrinkers
{
	//noinspection AccessorLikeMethodIsUnit
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Raw.Compound]), minSuccessful(100000), maxDiscardedFactor(5.0))
}


class EntityProps extends JUnitSuite with Checkers
	with ExpressionProperties[Entity] with RawGenerators // not shrinkable
{
	//noinspection AccessorLikeMethodIsUnit
	@Test def toPattern() : Unit = check(toPattern(arbitrary[Entity]), minSuccessful(100000), maxDiscardedFactor(5.0))
}


class EscapedLiteralProps extends JUnitSuite with Checkers
	with ExpressionProperties[EscapedLiteral] with RawGenerators with RawShrinkers
{
	//noinspection AccessorLikeMethodIsUnit
	@Test def toPattern() : Unit = check(toPattern(arbitrary[EscapedLiteral]), minSuccessful(100000), maxDiscardedFactor(5.0))
}
