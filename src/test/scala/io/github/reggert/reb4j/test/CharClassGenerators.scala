package io.github.reggert.reb4j.test

import org.scalacheck.Arbitrary
import Arbitrary.arbitrary
import org.scalacheck.Gen
import io.github.reggert.reb4j.charclass.PredefinedClass
import io.github.reggert.reb4j.charclass.SingleChar
import io.github.reggert.reb4j.charclass.MultiChar
import io.github.reggert.reb4j.charclass.CharRange
import io.github.reggert.reb4j.charclass.Intersection
import io.github.reggert.reb4j.charclass.CharClass
import io.github.reggert.reb4j.charclass.Union
import scala.collection.JavaConverters._


trait CharClassGenerators extends UtilGenerators {
	implicit val arbCharClass : Arbitrary[CharClass] = 
	  Arbitrary(Gen.sized{size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genCharClass })
	implicit val arbCharRange: Arbitrary[CharRange] = Arbitrary(genCharRange)
	implicit val arbIntersection: Arbitrary[Intersection] =
	  Arbitrary(Gen.sized{size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genIntersection })
	implicit val arbUnion: Arbitrary[Union] =
	  Arbitrary(Gen.sized{size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genUnion })
	implicit val arbMultiChar: Arbitrary[MultiChar] =
	  Arbitrary(Gen.sized{size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genMultiChar })
	implicit val arbSingleChar: Arbitrary[SingleChar] = Arbitrary(genSingleChar)
	implicit val arbPredefinedClass: Arbitrary[PredefinedClass] = Arbitrary(genPredefinedClass)
	
	
	def genCharClass(size : Int) : Gen[CharClass] =
	{
		require (size > 0)
		val nonInverted = size match {
			case 1 => 
		    	Gen.oneOf(genSingleChar, genCharRange, genPredefinedClass)
			case _ =>
		    	Gen.oneOf(
		    			Gen.lzy(genMultiChar(size)),
		    			Gen.lzy(genIntersection(size)),
		    			Gen.lzy(genUnion(size))
		    		)
		}
		for {
		  cc <- nonInverted
		  ccn <- Gen.oneOf(cc, cc.negated)
		} yield ccn
	}
	
	
	def genCharRange : Gen[CharRange] = for {
		first::last::Nil <- Gen.listOfN(2, arbitrary[Char])
	} yield if (first < last) CharClass.range(first, last) else CharClass.range(last, first)
	
	
	//noinspection ZeroIndexToHead
	private def genCombined[CombinedType <: CharClass](size : Int)(combine : (CharClass, CharClass) => CombinedType) : Gen[CombinedType] = {
		require(size >= 2)
		val sizesGen = size match
		{
			case 2 => Gen.const(1::1::Nil)
			case _ => genSizes(size) filter {_.length >= 2}
		}
		for {
			sizes <- sizesGen
			subtreeGens = for {s <- sizes} yield genCharClass(s) 
			subtreesGen = (Gen.const(Nil : List[CharClass]) /: subtreeGens) {(ssGen, sGen) =>
				for {
					ss <- ssGen
					s <- sGen
				} yield s::ss
			}
			subtrees <- subtreesGen
		} yield (combine(subtrees(0), subtrees(1)) /: subtrees.drop(2)) (combine)
	}
	
	
	def genIntersection(size : Int) : Gen[Intersection] = genCombined(size) {_ intersect _}
	
	
	def genUnion(size : Int) : Gen[Union] = genCombined(size) {_ union _} 
	  
	
	def genMultiChar(size : Int) : Gen[MultiChar] = {
	  require(size > 0)
	  for {chars <- Gen.listOfN(size, arbitrary[Char])} yield new MultiChar(fj.data.Set.iterableSet(fj.Ord.charOrd, chars.map(java.lang.Character.valueOf).asJava))
	}
	
	
	def genSingleChar : Gen[SingleChar] = for {
		c <- arbitrary[Char]
	} yield CharClass.character(c)
	
	
	def genPredefinedClass : Gen[PredefinedClass] = Gen.oneOf(
			CharClass.Perl.DIGIT,
			CharClass.Perl.SPACE,
			CharClass.Perl.WORD,
			CharClass.Posix.ALNUM,
			CharClass.Posix.ALPHA,
			CharClass.Posix.BLANK,
			CharClass.Posix.CONTROL,
			CharClass.Posix.DIGIT,
			CharClass.Posix.GRAPH,
			CharClass.Posix.HEX_DIGIT,
			CharClass.Posix.LOWER,
			CharClass.Posix.PRINT,
			CharClass.Posix.PUNCT,
			CharClass.Posix.SPACE,
			CharClass.Posix.UPPER,
			CharClass.Java.LOWER_CASE,
			CharClass.Java.MIRROR,
			CharClass.Java.UPPER_CASE,
			CharClass.Java.WHITESPACE,
			//CharClass.Unicode.block(arbitrary[UnicodeBlock]),
			CharClass.Unicode.Letter.LOWER_CASE,
			CharClass.Unicode.Letter.MODIFIER,
			CharClass.Unicode.Letter.OTHER,
			CharClass.Unicode.Letter.TITLE_CASE,
			CharClass.Unicode.Letter.UPPER_CASE,
			CharClass.Unicode.Mark.ENCLOSING,
			CharClass.Unicode.Mark.NONSPACING,
			CharClass.Unicode.Mark.SPACING_COMBINING,
			CharClass.Unicode.Number.DECIMAL_DIGIT,
			CharClass.Unicode.Number.LETTER,
			CharClass.Unicode.Number.OTHER,
			CharClass.Unicode.Other.CONTROL,
			CharClass.Unicode.Other.FORMAT,
			CharClass.Unicode.Other.NOT_ASSIGNED,
			CharClass.Unicode.Other.PRIVATE_USE,
			CharClass.Unicode.Other.SURROGATE,
			CharClass.Unicode.Punctuation.CLOSE,
			CharClass.Unicode.Punctuation.CONNECTOR,
			CharClass.Unicode.Punctuation.DASH,
			CharClass.Unicode.Punctuation.FINAL_QUOTE,
			CharClass.Unicode.Punctuation.INITIAL_QUOTE,
			CharClass.Unicode.Punctuation.OPEN,
			CharClass.Unicode.Punctuation.OTHER,
			CharClass.Unicode.Separator.LINE,
			CharClass.Unicode.Separator.PARAGRAPH,
			CharClass.Unicode.Separator.SPACE,
			CharClass.Unicode.Symbol.CURRENCY,
			CharClass.Unicode.Symbol.MATH,
			CharClass.Unicode.Symbol.MODIFIER,
			CharClass.Unicode.Symbol.OTHER
		)

}