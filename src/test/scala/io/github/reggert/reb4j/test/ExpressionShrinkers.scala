package io.github.reggert.reb4j.test

import org.scalacheck.Shrink
import Shrink._
import io.github.reggert.reb4j._
import io.github.reggert.reb4j.charclass._
import scala.collection.convert.decorateAll._
import scala.language.postfixOps


trait ExpressionShrinkers extends LiteralShrinkers with RawShrinkers {

	implicit val shrinkExpression : Shrink[Expression] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : SingleChar => Literal.literal(charclass.character) +: shrink(charclass)
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
		case charclass : SingleChar => Literal.literal(charclass.character) +: shrink(charclass)
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
		case charclass : SingleChar => Literal.literal(charclass.character) +: shrink(charclass)
		case charclass : CharClass => shrink(charclass)
		case group : Group =>
			(Some(group.nested) collect {case seq : Sequenceable => seq}) ++: shrink(group)
		case quantified : Quantified =>
			(Some(quantified.base) collect {case seq : Sequenceable => seq}) ++: shrink(quantified)
		case sequence : Sequence => sequence.components.asScala ++: shrink(sequence)
	}

	//noinspection ZeroIndexToHead
	implicit val shrinkAlternation : Shrink[Alternation] = Shrink { alternation =>
		for {
			alternatives <- shrink(alternation.alternatives.asScala.toSeq)
			if alternatives.length >= 2
		} yield ((alternatives(0) or alternatives(1)) /: alternatives.drop(2)) {_ or _}
	}
	
	//noinspection ZeroIndexToHead
	implicit val shrinkSequence : Shrink[Sequence] = Shrink { sequence =>
		for {
			components <- shrink(sequence.components.asScala.toSeq)
			if components.length >= 2
		} yield ((components(0) andThen components(1)) /: components.drop(2)) {_ andThen _}
	}
	
	private def shrinkGroupType[T <: Group](construct : Expression => T) = Shrink {group : T =>
		for {
			nested <- shrink(group.nested) 
			shrunk <- try {Some(construct(nested))} catch {case _ : UnboundedLookBehindException => None} 
		} yield shrunk
	}
	
	implicit val shrinkCapture: Shrink[Group.Capture] = shrinkGroupType(Group.capture)
	
	implicit val shrinkIndependent: Shrink[Group.Independent] = shrinkGroupType(Group.independent)
	
	implicit val shrinkNegativeLookAhead: Shrink[Group.NegativeLookAhead] = shrinkGroupType(Group.negativeLookAhead)
	
	implicit val shrinkNegativeLookBehind: Shrink[Group.NegativeLookBehind] = shrinkGroupType(Group.negativeLookBehind)
	
	implicit val shrinkNonCapturing: Shrink[Group.NonCapturing] = shrinkGroupType(Group.nonCapturing)
	
	implicit val shrinkPositiveLookAhead: Shrink[Group.PositiveLookAhead] = shrinkGroupType(Group.positiveLookAhead)
	
	implicit val shrinkPositiveLookBehind: Shrink[Group.PositiveLookBehind] = shrinkGroupType(Group.positiveLookBehind)
	
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
		case group : Group.Independent => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NegativeLookAhead => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NegativeLookBehind => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NonCapturing => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.PositiveLookAhead => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.PositiveLookBehind => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.DisableFlags => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.EnableFlags => Group.capture(group.nested) +: (for (g <- shrink(group)) yield g)
	}
	
	implicit val shrinkQuantifiable : Shrink[Quantifiable] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case group : Group => 
			(Some(group.nested) collect {case q : Quantifiable => q}) ++: shrink(group)
		case quantified : Quantified => quantified.base +: shrink(quantified)
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
		(for (repetitions <- shrink(quantified.repetitions) if repetitions > 0) yield quantified.base.repeat(repetitions, quantified.mode)) ++:
		(for (base <- shrink(quantified.base)) yield base.repeat(quantified.repetitions, quantified.mode))
	}
	
	implicit val shrinkRepeatRange : Shrink[Quantified.RepeatRange] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Mode.GREEDY} map {q => new Quantified.RepeatRange(q.base, q.minRepetitions, q.maxRepetitions, Quantified.Mode.GREEDY)}) ++:
		(for {
			minRepetitions <- shrink(quantified.minRepetitions) 
			if minRepetitions >= 0
			if Option(quantified.maxRepetitions).isEmpty || minRepetitions < quantified.maxRepetitions
		} yield new Quantified.RepeatRange(quantified.base, minRepetitions, quantified.maxRepetitions, quantified.mode)) ++:
		(for {
			maxRepetitions <- shrink(Option(quantified.maxRepetitions) map {_.intValue})
			if maxRepetitions.isDefined
			if maxRepetitions.get >= quantified.minRepetitions
		} yield new Quantified.RepeatRange(quantified.base, quantified.minRepetitions, maxRepetitions map {Integer.valueOf} orNull, quantified.mode)) ++:
		(for (base <- shrink(quantified.base)) yield base.repeat(quantified.minRepetitions, quantified.maxRepetitions, quantified.mode))
	}
	
	implicit val shrinkQuantified : Shrink[Quantified] = Shrink {
		case quantified : Quantified.AnyTimes => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.AtLeastOnce => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.Optional => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.RepeatExactly => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.RepeatRange => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
	}
}

object ExpressionShrinkers extends ExpressionShrinkers