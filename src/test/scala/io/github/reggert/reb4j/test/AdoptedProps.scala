package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4j.Adopted
import org.scalatest.junit.JUnitSuite
import org.scalatest.prop.Checkers
import org.junit.Test

class AdoptedProps extends JUnitSuite with Checkers 
	with ExpressionProperties[Adopted] with AdoptedGenerators 
{
	@Test def toPattern : Unit = check(toPattern(arbitrary[Adopted]), minSuccessful(100000), maxDiscarded(500000))
}