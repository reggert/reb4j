package io.github.reggert.reb4j.test

import io.github.reggert.reb4j.{Entity, Raw}
import org.scalacheck.{Arbitrary, Gen}


trait RawGenerators extends UtilGenerators with LiteralGenerators {
	implicit val arbEntity: Arbitrary[Entity] = Arbitrary(genEntity)
	implicit val arbRawCompound : Arbitrary[Raw.Compound] = 
		Arbitrary(Gen.sized {size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genRawCompound })
	implicit val arbEscapedLiteral: Arbitrary[Raw.EscapedLiteral] =
		Arbitrary(Gen.sized {size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genEscapedLiteral })
	implicit val arbRaw: Arbitrary[Raw] =
		Arbitrary(Gen.sized {size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genRaw })
	

	//noinspection ZeroIndexToHead
	def genRawCompound(size : Int) : Gen[Raw.Compound] = {
		require(size >= 2)
		val sizesGen = size match
		{
			case 2 => Gen.const(1::1::Nil)
			case _ => genSizes(size) filter {_.length >= 2}
		}
		for {
			sizes <- sizesGen
			subtreeGens = for {s <- sizes} yield genRaw(s) 
			subtreesGen = (Gen.const(Nil : List[Raw]) /: subtreeGens) {(ssGen, sGen) =>
				for {
					ss <- ssGen
					s <- sGen
				} yield s::ss
			}
			subtrees <- subtreesGen
		} yield ((subtrees(0) andThen subtrees(1)) /: subtrees.drop(2)) {_ andThen _}
	}
	
	def genRaw(size : Int) : Gen[Raw] = {
		require (size > 0)
		size match {
			case 1 => Gen.oneOf(genEscapedLiteral(1), genEntity)
			case _ => Gen.oneOf(genEscapedLiteral(size), Gen.lzy(genRawCompound(size)))
		}
	} 
		
	def genEntity : Gen[Entity] = Gen.oneOf(
			Entity.ANY_CHAR,
			Entity.LINE_BEGIN,
			Entity.LINE_END,
			Entity.WORD_BOUNDARY,
			Entity.NONWORD_BOUNDARY,
			Entity.INPUT_BEGIN,
			Entity.MATCH_END,
			Entity.INPUT_END_SKIP_EOL,
			Entity.INPUT_END
		)
		
	def genEscapedLiteral(size : Int) : Gen[Raw.EscapedLiteral] = 
		genLiteral(size) map {lit => new Raw.EscapedLiteral(lit)}
}


object RawGenerators extends RawGenerators