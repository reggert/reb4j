package io.github.reggert.reb4j;

import io.github.reggert.reb4j.Quantified.Mode;
import fj.data.LazyString;

/**
 * Class representing special pre-defined expressions.
 */
public class Entity extends Raw implements Quantifiable
{
	private static final long serialVersionUID = 1L;

	private Entity(final String rawExpression)
	{super(LazyString.str(rawExpression));}
	
	@Override
	public final Quantified.AnyTimes anyTimes(final Mode mode)
	{return new Quantified.AnyTimes(this, mode);}
	
	@Override
	public final Quantified.AnyTimes anyTimes()
	{return anyTimes(Mode.GREEDY);}

	@Override
	@Deprecated
	public final Quantified.AnyTimes anyTimesReluctantly()
	{return Quantified.anyTimesReluctantly(this);}

	@Override
	@Deprecated
	public final Quantified.AnyTimes anyTimesPossessively()
	{return Quantified.anyTimesPossessively(this);}

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
	
	@Override
	public final Integer boundedLength() 
	{
		return 1;
	}
	
	@Override 
	public final boolean repetitionInvalidatesBounds()  
	{
		return false;
	}
	
	@Override 
	public boolean possiblyZeroLength()
	{
		return true;
	}
	
	/**
	 * Matches any single character.
	 */
	public static final Entity ANY_CHAR = new Entity(".")
	{
		private static final long serialVersionUID = 1L;

		@Override 
		public final boolean possiblyZeroLength()
		{
			return false;
		}
	};

	/**
	 * Matches the beginning of a line.
	 */
	public static final Entity LINE_BEGIN = new Entity("^");

	/**
	 * Matches the end of a line.
	 */
	public static final Entity LINE_END = new Entity("$");

	/**
	 * Matches a word boundary.
	 */
	public static final Entity WORD_BOUNDARY = new Entity("\\b");

	/**
	 * Matches a non-word-boundary.
	 */
	public static final Entity NONWORD_BOUNDARY = new Entity("\\B");

	/**
	 * Matches the beginning of input.
	 */
	public static final Entity INPUT_BEGIN = new Entity("\\A");

	/**
	 * Matches the end of the previous match.
	 */
	public static final Entity MATCH_END = new Entity("\\G");

	/**
	 * Matches the end of input, skipping end-of-line markers.
	 */
	public static final Entity INPUT_END_SKIP_EOL = new Entity("\\Z");

	/**
	 * Matches the end of input.
	 */
	public static final Entity INPUT_END = new Entity("\\z");

}