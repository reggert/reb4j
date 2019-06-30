package io.github.reggert.reb4j.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4j.{Literal,CharLiteral,StringLiteral}

trait LiteralGenerators {
	implicit val arbCharLiteral: Arbitrary[CharLiteral] = Arbitrary(genCharLiteral)
	implicit val arbStringLiteral: Arbitrary[StringLiteral] =
		Arbitrary(Gen.sized {size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genStringLiteral })
	implicit val arbLiteral: Arbitrary[Literal] =
		Arbitrary(Gen.sized {size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genLiteral })
	
	
	def genCharLiteral : Gen[CharLiteral] = 
		for {c <- arbitrary[Char]} yield Literal.literal(c)
	
	def genStringLiteral(size : Int) : Gen[StringLiteral] = {
		require(size > 0)
		for {s <- Gen.listOfN(size, arbitrary[Char])} yield Literal.literal(s.mkString)
	}
	
	def genLiteral(size : Int) : Gen[Literal] = size match {
			case 1 => genCharLiteral
			case _ => genStringLiteral(size)
		} 
}