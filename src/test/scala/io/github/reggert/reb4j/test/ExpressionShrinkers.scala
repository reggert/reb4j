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
		case alternation : Alternation => alternation.alternatives.asScala ++: shrink(alternation)
		case group : Group => group.nested +: shrink(group)
		case quantified : Quantified => quantified.base +: shrink(quantified)
		case sequence : Sequence => sequence.components.asScala ++: shrink(sequence)
		case adopted : Adopted => shrink(adopted)
	}
	
	implicit val shrinkAlternative : Shrink[Alternative] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => alternation.alternatives.asScala ++: shrink(alternation)
		case group : Group =>
			(Some(group.nested) collect {case alt : Alternative => alt}) ++: shrink(group)
		case quantified : Quantified =>
			(Some(quantified.base) collect {case alt : Alternative => alt}) ++: shrink(quantified)
		case sequence : Sequence => 
			(sequence.components.asScala collect {case alt : Alternative => alt}) ++: shrink(sequence)
	}
	
	implicit val shrinkSequenceable : Shrink[Sequenceable] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case group : Group =>
			(Some(group.nested) collect {case seq : Sequenceable => seq}) ++: shrink(group)
		case quantified : Quantified =>
			(Some(quantified.base) collect {case seq : Sequenceable => seq}) ++: shrink(quantified)
		case sequence : Sequence => sequence.components.asScala ++: shrink(sequence)
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
	
	implicit val shrinkDisableFlags : Shrink[Group.DisableFlags] = Shrink {g =>
		(for (f <- shrink(g.flags.asScala)) yield Group.disableFlags(g.nested, f.toSeq : _*)) ++:
		(for (n <- shrink(g.nested)) yield Group.disableFlags(n, g.flags.asScala.toSeq : _*))
	}
	
	implicit val shrinkEnableFlags : Shrink[Group.EnableFlags] = Shrink {g =>
		(for (f <- shrink(g.flags.asScala)) yield Group.enableFlags(g.nested, f.toSeq : _*)) ++:
		(for (n <- shrink(g.nested)) yield Group.enableFlags(n, g.flags.asScala.toSeq : _*))
	}
	
	implicit val shrinkGroup : Shrink[Group] = Shrink {
		case group : Group.Capture => for (g <- shrink(group)) yield g
		case group : Group.Independent => for (g <- shrink(group)) yield g
		case group : Group.NegativeLookAhead => for (g <- shrink(group)) yield g
		case group : Group.NegativeLookBehind => for (g <- shrink(group)) yield g
		case group : Group.NonCapturing => for (g <- shrink(group)) yield g
		case group : Group.PositiveLookAhead => for (g <- shrink(group)) yield g
		case group : Group.PositiveLookBehind => for (g <- shrink(group)) yield g
		case group : Group.DisableFlags => for (g <- shrink(group)) yield g
		case group : Group.EnableFlags => for (g <- shrink(group)) yield g
	}
	
	implicit val shrinkAnyTimes : Shrink[Quantified.AnyTimes] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => q.base.anyTimes()}) ++:
		(for (base <- shrink(quantified.base)) yield base.anyTimes(quantified.mode))
	}
	
	implicit val shrinkAtLeastOnce : Shrink[Quantified.AtLeastOnce] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => q.base.atLeastOnce()}) ++:
		(for (base <- shrink(quantified.base)) yield base.atLeastOnce(quantified.mode))
	}
	
	implicit val shrinkOptional : Shrink[Quantified.Optional] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => q.base.optional()}) ++:
		(for (base <- shrink(quantified.base)) yield base.optional(quantified.mode))
	}
	
	implicit val shrinkRepeatExactly : Shrink[Quantified.RepeatExactly] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => q.base.repeat(q.repetitions)}) ++:
		(for (base <- shrink(quantified.base)) yield base.repeat(quantified.repetitions, quantified.mode))
	}
	
	implicit val shrinkRepeatRange : Shrink[Quantified.RepeatRange] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => new Quantified.RepeatRange(q.base, q.minRepetitions, q.maxRepetitions, Quantified.Mode.GREEDY)}) ++:
		(for (base <- shrink(quantified.base)) yield base.repeat(quantified.minRepetitions, quantified.maxRepetitions, quantified.mode))
	}
	
	implicit val shrinkQuantified : Shrink[Quantified] = Shrink {
		case quantified : Quantified.AnyTimes => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.AtLeastOnce => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.Optional => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.RepeatExactly => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.RepeatRange => for (q <- shrink(quantified)) yield q
	}
}