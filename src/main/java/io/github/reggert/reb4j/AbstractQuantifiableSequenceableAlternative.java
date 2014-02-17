package io.github.reggert.reb4j;

import io.github.reggert.reb4j.Quantified.Mode;

/**
 * Extension to {@link AbstractSequenceableAlternative} providing
 * the canonical implementation of {@link Quantifiable}. 
 * 
 * Note that it delegates to static methods of {@link Quantified}.
 */
public abstract class AbstractQuantifiableSequenceableAlternative 
	extends AbstractSequenceableAlternative implements Quantifiable
{
	private static final long serialVersionUID = 1L;

	@Override
	public final Quantified.AnyTimes anyTimes(final Mode mode)
	{return new Quantified.AnyTimes(this, mode);}
	
	@Override
	public final Quantified.AnyTimes anyTimes()
	{return anyTimes(Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.AnyTimes anyTimesReluctantly()
	{return anyTimes(Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.AnyTimes anyTimesPossessively()
	{return anyTimes(Mode.POSSESSIVE);}

	@Override
	public final Quantified.AtLeastOnce atLeastOnce(final Mode mode)
	{return new Quantified.AtLeastOnce(this, mode);}
	
	@Override
	public final Quantified.AtLeastOnce atLeastOnce()
	{return atLeastOnce(Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.AtLeastOnce atLeastOnceReluctantly()
	{return atLeastOnce(Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.AtLeastOnce atLeastOncePossessively()
	{return atLeastOnce(Mode.POSSESSIVE);}

	@Override
	public final Quantified.Optional optional(final Mode mode)
	{return new Quantified.Optional(this, mode);}
	
	@Override
	public final Quantified.Optional optional()
	{return optional(Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.Optional optionalReluctantly()
	{return optional(Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.Optional optionalPossessively()
	{return optional(Mode.POSSESSIVE);}

	@Override
	public final Quantified.RepeatExactly repeat(final int n, final Mode mode)
	{return new Quantified.RepeatExactly(this, n, mode);}
	
	@Override
	public final Quantified.RepeatExactly repeat(final int n)
	{return repeat(n, Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.RepeatExactly repeatReluctantly(final int n)
	{return repeat(n, Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.RepeatExactly repeatPossessively(final int n)
	{return repeat(n, Mode.POSSESSIVE);}

	@Override
	public final Quantified.RepeatRange repeat(final int min, final int max, final Mode mode)
	{return new Quantified.RepeatRange(this, min, max, mode);}
	
	@Override
	public final Quantified.RepeatRange repeat(final int min, final int max)
	{return repeat(min, max, Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.RepeatRange repeatReluctantly(final int min, final int max)
	{return repeat(min, max, Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.RepeatRange repeatPossessively(final int min, final int max)
	{return repeat(min, max, Mode.POSSESSIVE);}

	@Override
	public final Quantified.RepeatRange atLeast(final int n, final Mode mode)
	{return new Quantified.RepeatRange(this, n, null, mode);}
	
	@Override
	public final Quantified.RepeatRange atLeast(final int n)
	{return atLeast(n, Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.RepeatRange atLeastReluctantly(final int n)
	{return atLeast(n, Mode.RELUCTANT);}

	@Override
	@Deprecated
	public final Quantified.RepeatRange atLeastPossessively(final int n)
	{return atLeast(n, Mode.POSSESSIVE);}

}
