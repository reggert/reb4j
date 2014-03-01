package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import Shrink._
import org.scalacheck.util.Buildable._
import io.github.reggert.reb4j.Raw
import scala.collection.convert.decorateAll._

trait RawShrinkers extends LiteralShrinkers {
	implicit val shrinkEscapedLiteral : Shrink[Raw.EscapedLiteral] = Shrink {escapedLiteral =>
		for {
			literal <- shrink(escapedLiteral.literal)
		} yield new Raw.EscapedLiteral(literal)
	}
	
	implicit val shrinkCompoundRaw : Shrink[Raw.Compound] = Shrink {compoundRaw =>
		for {
			components <- shrink(compoundRaw.components.asScala.toSeq)
			if components.length >= 2
		} yield ((components(0) andThen components(1)) /: components.drop(2)) {_ andThen _}
	}
	
	implicit val shrinkRaw : Shrink[Raw] = Shrink {
		case escapedLiteral : Raw.EscapedLiteral => shrink(escapedLiteral)
		case compoundRaw : Raw.Compound => compoundRaw.components.asScala ++: shrink(compoundRaw)
		case _ => Stream.empty
	}
}


object RawShrinkers extends RawShrinkers