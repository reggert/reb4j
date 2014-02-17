package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import org.scalacheck.Shrink._
import io.github.reggert.reb4j.charclass.{Intersection, MultiChar, Union}
import io.github.reggert.reb4j.charclass.CharClass
import scala.collection.convert.decorateAll._

trait CharClassShrinkers {
	
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
}