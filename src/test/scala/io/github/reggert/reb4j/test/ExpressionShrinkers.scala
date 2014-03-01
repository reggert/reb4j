package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import Shrink._
import io.github.reggert.reb4j._
import io.github.reggert.reb4j.charclass._
import scala.collection.convert.decorateAll._

trait ExpressionShrinkers extends LiteralShrinkers with RawShrinkers {

	implicit val shrinkExpression : Shrink[Expression] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => shrink(alternation)
		case group : Group => shrink(group)
		case quantified : Quantified => shrink(quantified)
		case sequence : Sequence => shrink(sequence)
		case adopted : Adopted => shrink(adopted)
	}
	
	implicit val shrinkAlternative : Shrink[Alternative] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => shrink(alternation)
		case group : Group => shrink(group)
		case quantified : Quantified => shrink(quantified)
		case sequence : Sequence => shrink(sequence)
	}
	
	implicit val shrinkSequenceable : Shrink[Sequenceable] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case group : Group => shrink(group)
		case quantified : Quantified => shrink(quantified)
		case sequence : Sequence => shrink(sequence)
	}

	implicit val shrinkAlternation : Shrink[Alternation] = Shrink {alternation =>
		for {
			alternatives <- shrink(alternation.alternatives.asScala.toSeq)
			if alternatives.length >= 2
		} yield ((alternatives(0) or alternatives(1)) /: (alternatives.drop(2))) {_ or _}
	}
	
	implicit val shrinkSequence : Shrink[Sequence] = Shrink {sequence =>
		for {
			components <- shrink(sequence.components.asScala.toSeq)
			if components.length >= 2
		} yield ((components(0) andThen components(1)) /: (components.drop(2))) {_ andThen _}
	}
	
	private def shrinkGroupType[T <: Group](construct : Expression => T) = Shrink {group : T =>
		for (nested <- shrink(group.nested)) yield construct(nested)
	}
	
	implicit val shrinkCapture = shrinkGroupType(Group.capture)
	
	implicit val shrinkIndependent = shrinkGroupType(Group.independent)
	
	implicit val shrinkNegativeLookAhead = shrinkGroupType(Group.negativeLookAhead)
	
	implicit val shrinkNegativeLookBehind = shrinkGroupType(Group.negativeLookBehind)
	
	implicit val shrinkNonCapturing = shrinkGroupType(Group.nonCapturing)
	
	implicit val shrinkPositiveLookAhead = shrinkGroupType(Group.positiveLookAhead)
	
	implicit val shrinkPositiveLookBehind = shrinkGroupType(Group.positiveLookBehind)
	
	implicit val shrinkDisableFlags : Shrink[Group.DisableFlags] = Shrink {
		g => for (n <- shrink(g.nested)) yield Group.disableFlags(n, g.flags.asScala.toSeq : _*)
	}
	
	implicit val shrinkEnableFlags : Shrink[Group.EnableFlags] = Shrink {
		g => for (n <- shrink(g.nested)) yield Group.enableFlags(n, g.flags.asScala.toSeq : _*)
	}
	
	implicit val shrinkGroup : Shrink[Group] = Shrink {
		case group : Group.Capture => for (g <- shrinkCapture.shrink(group)) yield g
		case group : Group.Independent => for (g <- shrinkIndependent.shrink(group)) yield g
		case group : Group.NegativeLookAhead => for (g <- shrinkNegativeLookAhead.shrink(group)) yield g
		case group : Group.NegativeLookBehind => for (g <- shrinkNegativeLookBehind.shrink(group)) yield g
		case group : Group.NonCapturing => for (g <- shrinkNonCapturing.shrink(group)) yield g
		case group : Group.PositiveLookAhead => for (g <- shrinkPositiveLookAhead.shrink(group)) yield g
		case group : Group.PositiveLookBehind => for (g <- shrinkPositiveLookBehind.shrink(group)) yield g
		case group : Group.DisableFlags => for (g <- shrinkDisableFlags.shrink(group)) yield g
		case group : Group.EnableFlags => for (g <- shrinkEnableFlags.shrink(group)) yield g
	}
	
	implicit val shrinkAnyTimes : Shrink[Quantified.AnyTimes] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield nested.anyTimes(quantified.mode)
	}
	
	implicit val shrinkAtLeastOnce : Shrink[Quantified.AtLeastOnce] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield nested.atLeastOnce(quantified.mode)
	}
	
	implicit val shrinkOptional : Shrink[Quantified.Optional] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield nested.optional(quantified.mode)
	}
	
	implicit val shrinkRepeatExactly : Shrink[Quantified.RepeatExactly] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield nested.repeat(quantified.repetitions, quantified.mode)
	}
	
	implicit val shrinkRepeatRange : Shrink[Quantified.RepeatRange] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield new Quantified.RepeatRange(nested, quantified.minRepetitions, quantified.maxRepetitions, quantified.mode)
	}
	
	implicit val shrinkQuantified : Shrink[Quantified] = Shrink {
		case quantified : Quantified.AnyTimes => for (q <- shrinkAnyTimes.shrink(quantified)) yield q
		case quantified : Quantified.AtLeastOnce => for (q <- shrinkAtLeastOnce.shrink(quantified)) yield q
		case quantified : Quantified.Optional => for (q <- shrinkOptional.shrink(quantified)) yield q
		case quantified : Quantified.RepeatExactly => for (q <- shrinkRepeatExactly.shrink(quantified)) yield q
		case quantified : Quantified.RepeatRange => for (q <- shrinkRepeatRange.shrink(quantified)) yield q
	}
}