package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import org.scalacheck.Shrink._
import io.github.reggert.reb4j.charclass._
import scala.collection.convert.decorateAll._
import io.github.reggert.reb4j.charclass.SingleChar

trait CharClassShrinkers extends CharShrinkers {
	
	implicit val shrinkUnion : Shrink[Union] = Shrink {
		case union : Union => for {
			subsets <- shrink(union.subsets.asScala.toSeq)
			if subsets.length >= 2
		} yield ((subsets(0) union subsets(1)) /: subsets.drop(2)) {_ union _}
	}
	
	implicit val shrinkIntersection : Shrink[Intersection] = Shrink {
		case x : Intersection => for {
			supersets <- shrink(x.supersets.asScala.toSeq)
			if supersets.length >= 2
		} yield ((supersets(0) intersect supersets(1)) /: supersets.drop(2)) {_ intersect _}
	}
	
	implicit val shrinkMultiChar : Shrink[MultiChar] = Shrink {
		case multichar : MultiChar => for {
			chars <- shrink(multichar.characters.asScala.toSeq)
			if chars.size >= 2
		} yield new MultiChar(fj.data.Set.iterableSet(fj.Ord.charOrd, chars.asJava))
	}
	
	implicit val shrinkSingleChar : Shrink[SingleChar] = Shrink {singlechar =>
		shrink(singlechar.character) map {CharClass.character}
	}
	
	implicit val shrinkCharRange : Shrink[CharRange] = Shrink {charRange =>
		for {
			first <- charRange.first #:: shrink(charRange.first)
			last <- charRange.last #:: shrink(charRange.last)
			if first <= last
			if first != charRange.first || last != charRange.last
		} yield CharClass.range(first, last)
	}
	
	implicit val shrinkPredefinedClass : Shrink[PredefinedClass] = Shrink {
		case _ : NamedPredefinedClass => Stream(CharClass.Perl.DIGIT)
		case _ => Stream.empty
	}
	
	implicit val shrinkCharClass : Shrink[CharClass] = Shrink {
		case multichar : MultiChar => 
			(multichar.characters.asScala map {c => CharClass.character(Character.valueOf(c))}) ++: shrink(multichar)
		case singlechar : SingleChar => shrink(singlechar)
		case union : Union => union.subsets.asScala ++: shrink(union)
		case intersection : Intersection => intersection.supersets.asScala ++: shrink(intersection)
		case range : CharRange => CharClass.character(range.first) +: CharClass.character(range.last) +: shrink(range)
		case negated : Negated[_] => negated.positive +: shrink(negated.positive)
		case predefined : PredefinedClass => CharClass.character('a') +: shrink(predefined)
	}
}

object CharClassShrinkers extends CharClassShrinkers
