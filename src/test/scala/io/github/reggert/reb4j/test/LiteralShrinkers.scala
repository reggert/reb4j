package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import Shrink._
import io.github.reggert.reb4j.{Literal,StringLiteral,CharLiteral}

trait LiteralShrinkers extends CharShrinkers {
	
	implicit val shrinkStringLiteral : Shrink[StringLiteral] = Shrink {stringLiteral =>
		for {
			s <- shrink(stringLiteral.unescaped)
		} yield Literal.literal(s)
	}
	
	implicit val shrinkCharLiteral : Shrink[CharLiteral] = Shrink {charLiteral =>
		shrink(charLiteral.unescapedChar) map {Literal.literal}
	}
	
	implicit val shrinkLiteral : Shrink[Literal] = Shrink {
		case stringLiteral : StringLiteral => shrink(stringLiteral)
		case charLiteral : CharLiteral => shrink(charLiteral)
		case _ => Stream.empty
	}
}


object LiteralShrinkers extends LiteralShrinkers