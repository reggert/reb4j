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
	public final Quantified anyTimes(final Mode mode)
	{return Quantified.anyTimes(this, mode);}
	
	@Override
	public final Quantified anyTimes()
	{return Quantified.anyTimes(this);}

	@Override
	@Deprecated
	public final Quantified anyTimesReluctantly()
	{return Quantified.anyTimesReluctantly(this);}

	@Override
	@Deprecated
	public final Quantified anyTimesPossessively()
	{return Quantified.anyTimesPossessively(this);}

	@Override
	public final Quantified atLeastOnce(final Mode mode)
	{return Quantified.atLeastOnce(this, mode);}
	
	@Override
	public final Quantified atLeastOnce()
	{return Quantified.atLeastOnce(this);}

	@Override
	@Deprecated
	public final Quantified atLeastOnceReluctantly()
	{return Quantified.atLeastOnceReluctantly(this);}

	@Override
	@Deprecated
	public final Quantified atLeastOncePossessively()
	{return Quantified.atLeastOncePossessively(this);}

	@Override
	public final Quantified optional(final Mode mode)
	{return Quantified.optional(this, mode);}
	
	@Override
	public final Quantified optional()
	{return Quantified.optional(this);}

	@Override
	@Deprecated
	public final Quantified optionalReluctantly()
	{return Quantified.optionalReluctantly(this);}

	@Override
	@Deprecated
	public final Quantified optionalPossessively()
	{return Quantified.optionalPossessively(this);}

	@Override
	public final Quantified repeat(final int n, final Mode mode)
	{return Quantified.repeat(this, n, mode);}
	
	@Override
	public final Quantified repeat(final int n)
	{return Quantified.repeat(this, n);}

	@Override
	@Deprecated
	public final Quantified repeatReluctantly(final int n)
	{return Quantified.repeatReluctantly(this, n);}

	@Override
	@Deprecated
	public final Quantified repeatPossessively(final int n)
	{return Quantified.repeatPossessively(this, n);}

	@Override
	public final Quantified repeat(final int min, final int max, final Mode mode)
	{return Quantified.repeat(this, min, max, mode);}
	
	@Override
	public final Quantified repeat(final int min, final int max)
	{return Quantified.repeat(this, min, max);}

	@Override
	@Deprecated
	public final Quantified repeatReluctantly(final int min, final int max)
	{return Quantified.repeatReluctantly(this, min, max);}

	@Override
	@Deprecated
	public final Quantified repeatPossessively(final int min, final int max)
	{return Quantified.repeatPossessively(this, min, max);}

	@Override
	public final Quantified atLeast(final int n, final Mode mode)
	{return Quantified.atLeast(this, n, mode);}
	
	@Override
	public final Quantified atLeast(final int n)
	{return Quantified.atLeast(this, n);}

	@Override
	@Deprecated
	public final Quantified atLeastReluctantly(final int n)
	{return Quantified.atLeastReluctantly(this, n);}

	@Override
	@Deprecated
	public final Quantified atLeastPossessively(final int n)
	{return Quantified.atLeastPossessively(this, n);}

}
