package io.github.reggert.reb4j;

import fj.data.LazyString;

/**
 * Expression that has a quantifier attached to it.
 */
public final class Quantified extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	public final Quantifiable base;
	private final LazyString quantifier;
	
	public static enum Mode
	{
		GREEDY(LazyString.empty),
		RELUCTANT(LazyString.str("?")),
		POSSESSIVE(LazyString.str("+"));
		
		public final LazyString symbol;
		
		private Mode(final LazyString symbol)
		{this.symbol = symbol;}
	}
	
	private Quantified(final Quantifiable base, final LazyString quantifier)
	{
		if (base == null) throw new NullPointerException("base");
		if (quantifier == null) throw new NullPointerException("quantifier");
		this.base = base;
		this.quantifier = quantifier;
	}
	
	public String quantifier()
	{return quantifier.toString();}
	
	public static Quantified anyTimes(final Quantifiable base, final Mode mode)
	{
		return new Quantified(base, mode.symbol);
	}
	
	/**
	 * @see Quantifiable#anyTimes()
	 */
	public static Quantified anyTimes(final Quantifiable base)
	{
		return anyTimes(base, Mode.GREEDY);
	}
	
	/**
	 * @see Quantifiable#anyTimesReluctantly()
	 */
	@Deprecated
	public static Quantified anyTimesReluctantly(final Quantifiable base)
	{
		return anyTimes(base, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#anyTimesPossessively()
	 */
	@Deprecated
	public static Quantified anyTimesPossessively(final Quantifiable base)
	{
		return anyTimes(base, Mode.POSSESSIVE);
	}
	
	
	public static Quantified atLeastOnce(final Quantifiable base, final Mode mode)
	{
		return new Quantified(base, mode.symbol);
	}
	
	/**
	 * @see Quantifiable#atLeastOnce()
	 */
	public static Quantified atLeastOnce(final Quantifiable base)
	{
		return atLeastOnce(base, Mode.GREEDY);
	}
	
	/**
	 * @see Quantifiable#atLeastOnceReluctantly()
	 */
	@Deprecated
	public static Quantified atLeastOnceReluctantly(final Quantifiable base)
	{
		return atLeastOnce(base, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#atLeastOncePossessively()
	 */
	@Deprecated
	public static Quantified atLeastOncePossessively(final Quantifiable base)
	{
		return atLeastOnce(base, Mode.POSSESSIVE);
	}
	
	public static Quantified optional(final Quantifiable base, final Mode mode)
	{
		return new Quantified(base, mode.symbol);
	}
	
	/**
	 * @see Quantifiable#optional()
	 */
	public static Quantified optional(final Quantifiable base)
	{
		return optional(base, Mode.GREEDY);
	}
	
	/**
	 * @see Quantifiable#optionalReluctantly()
	 */
	@Deprecated
	public static Quantified optionalReluctantly(final Quantifiable base)
	{
		return optional(base, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#optionalPossessively()
	 */
	@Deprecated
	public static Quantified optionalPossessively(final Quantifiable base)
	{
		return optional(base, Mode.POSSESSIVE);
	}
	
	
	public static Quantified repeat(final Quantifiable base, final int n, final Mode mode)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append("}").append(mode.symbol));
	}
	
	/**
	 * @see Quantifiable#repeat(int)
	 */
	public static Quantified repeat(final Quantifiable base, final int n)
	{
		return repeat(base, n, Mode.GREEDY);
	}
	
	/**
	 * @see Quantifiable#repeatReluctantly(int)
	 */
	@Deprecated
	public static Quantified repeatReluctantly(final Quantifiable base, final int n)
	{
		return repeat(base, n, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#repeatPossessively(int)
	 */
	@Deprecated
	public static Quantified repeatPossessively(final Quantifiable base, final int n)
	{
		return repeat(base, n, Mode.POSSESSIVE);
	}
	
	public static Quantified repeat(final Quantifiable base, final int min, final int max, final Mode mode)
	{
		return new Quantified(
				base, 
				LazyString.str("{")
					.append(Integer.toString(min))
					.append(",")
					.append(Integer.toString(max))
					.append("}")
					.append(mode.symbol)
			); 
	}
	
	/**
	 * @see Quantifiable#repeat(int, int)
	 */
	public static Quantified repeat(final Quantifiable base, final int min, final int max)
	{
		return repeat(base, min, max, Mode.GREEDY); 
	}
	
	/**
	 * @see Quantifiable#repeatReluctantly(int, int)
	 */
	@Deprecated
	public static Quantified repeatReluctantly(final Quantifiable base, final int min, final int max)
	{
		return repeat(base, min, max, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#repeatPossessively(int, int)
	 */
	@Deprecated
	public static Quantified repeatPossessively(final Quantifiable base, final int min, final int max)
	{
		return repeat(base, min, max, Mode.POSSESSIVE);
	}
	
	public static Quantified atLeast(final Quantifiable base, final int n, final Mode mode)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append(",}").append(mode.symbol));
	}
	
	/**
	 * @see Quantifiable#atLeast(int)
	 */
	public static Quantified atLeast(final Quantifiable base, final int n)
	{
		return atLeast(base, n, Mode.GREEDY);
	}
	
	/**
	 * @see Quantifiable#atLeastReluctantly(int)
	 */
	@Deprecated
	public static Quantified atLeastReluctantly(final Quantifiable base, final int n)
	{
		return atLeast(base, n, Mode.RELUCTANT);
	}
	
	/**
	 * @see Quantifiable#atLeastPossessively(int)
	 */
	@Deprecated
	public static Quantified atLeastPossessively(final Quantifiable base, final int n)
	{
		return atLeast(base, n, Mode.POSSESSIVE);
	}

	@Override
	public LazyString expression()
	{
		return base.expression().append(quantifier);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + base.hashCode();
		result = prime * result + quantifier.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Quantified other = (Quantified) obj;
		return base.equals(other.base) && quantifier.equals(other.quantifier);
	}

}
