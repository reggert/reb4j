package io.github.reggert.reb4j;

import fj.data.LazyString;

/**
 * Expression that has a quantifier attached to it.
 */
public abstract class Quantified extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 2L;
	public final Quantifiable base;
	private final LazyString quantifier;
	public final Mode mode;
	
	public static enum Mode
	{
		GREEDY(LazyString.empty),
		RELUCTANT(LazyString.str("?")),
		POSSESSIVE(LazyString.str("+"));
		
		public final LazyString symbol;
		
		private Mode(final LazyString symbol)
		{this.symbol = symbol;}
	}
	
	private Quantified(final Quantifiable base, final Mode mode, final LazyString quantifier)
	{
		if (base == null) throw new NullPointerException("base");
		if (quantifier == null) throw new NullPointerException("quantifier");
		this.base = base;
		this.quantifier = quantifier;
		this.mode = mode;
	}
	
	public String quantifier()
	{return quantifier.toString();}
	
	
	public static final class AnyTimes extends Quantified
	{
		private static final long serialVersionUID = 1L;
		private static final LazyString SYMBOL = LazyString.str("*");

		public AnyTimes(final Quantifiable base, final Mode mode)
		{
			super(base, mode, SYMBOL.append(mode.symbol));
		}

		@Override
		public Integer boundedLength() 
		{
			return null;
		}

		@Override
		public boolean repetitionInvalidatesBounds() 
		{
			return true;
		}

		@Override
		public boolean possiblyZeroLength() 
		{
			return true;
		}
	}
	
	@Deprecated
	public static AnyTimes anyTimes(final Quantifiable base)
	{
		return new AnyTimes(base, Mode.GREEDY);
	}
	
	@Deprecated
	public static AnyTimes anyTimesReluctantly(final Quantifiable base)
	{
		return new AnyTimes(base, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static AnyTimes anyTimesPossessively(final Quantifiable base)
	{
		return new AnyTimes(base, Mode.POSSESSIVE);
	}
	
	public static final class AtLeastOnce extends Quantified
	{
		private static final long serialVersionUID = 1L;
		private static final LazyString SYMBOL = LazyString.str("+");

		public AtLeastOnce(final Quantifiable base, final Mode mode)
		{
			super(base, mode, SYMBOL.append(mode.symbol));
		}

		@Override
		public Integer boundedLength() 
		{
			return null;
		}

		@Override
		public boolean repetitionInvalidatesBounds() 
		{
			return false;
		}

		@Override
		public boolean possiblyZeroLength()
		{
			return base.possiblyZeroLength();
		}
	}
	
	@Deprecated
	public static AtLeastOnce atLeastOnce(final Quantifiable base)
	{
		return new AtLeastOnce(base, Mode.GREEDY);
	}
	
	@Deprecated
	public static AtLeastOnce atLeastOnceReluctantly(final Quantifiable base)
	{
		return new AtLeastOnce(base, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static AtLeastOnce atLeastOncePossessively(final Quantifiable base)
	{
		return new AtLeastOnce(base, Mode.POSSESSIVE);
	}
	
	public static final class Optional extends Quantified
	{
		private static final long serialVersionUID = 1L;
		private static final LazyString SYMBOL = LazyString.str("?");

		public Optional(final Quantifiable base, final Mode mode)
		{
			super(base, mode, SYMBOL.append(mode.symbol));
		}

		@Override
		public Integer boundedLength() 
		{
			return base.boundedLength();
		}

		@Override
		public boolean repetitionInvalidatesBounds() 
		{
			return true;
		}

		@Override
		public boolean possiblyZeroLength() 
		{
			return true;
		}
	}
	
	@Deprecated
	public static Optional optional(final Quantifiable base)
	{
		return new Optional(base, Mode.GREEDY);
	}
	
	@Deprecated
	public static Optional optionalReluctantly(final Quantifiable base)
	{
		return new Optional(base, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static Optional optionalPossessively(final Quantifiable base)
	{
		return new Optional(base, Mode.POSSESSIVE);
	}
	
	public static final class RepeatExactly extends Quantified
	{
		private static final long serialVersionUID = 1L;
		public final int repetitions;

		public RepeatExactly(final Quantifiable base, final int n, final Mode mode)
		{
			super(base, mode, LazyString.str("{").append(Integer.toString(n)).append("}").append(mode.symbol));
			this.repetitions = n;
		}

		@Override
		public Integer boundedLength() 
		{
			if (base.repetitionInvalidatesBounds())
				return null;
			final Integer baseLength = base.boundedLength();
			if (baseLength == null)
				return null;
			final long maximumLength = baseLength.longValue() * repetitions;
			if (maximumLength <= 0xfffffffL) // arbitrary value from Pattern source code
				return (int)maximumLength;
			return null;
		}

		@Override
		public boolean repetitionInvalidatesBounds() 
		{
			return base.repetitionInvalidatesBounds();
		}

		@Override
		public boolean possiblyZeroLength() 
		{
			return repetitions == 0 || base.possiblyZeroLength();
		}
	}
	
	@Deprecated
	public static RepeatExactly repeat(final Quantifiable base, final int n)
	{
		return new RepeatExactly(base, n, Mode.GREEDY);
	}
	
	@Deprecated
	public static RepeatExactly repeatReluctantly(final Quantifiable base, final int n)
	{
		return new RepeatExactly(base, n, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static RepeatExactly repeatPossessively(final Quantifiable base, final int n)
	{
		return new RepeatExactly(base, n, Mode.POSSESSIVE);
	}
	
	public static final class RepeatRange extends Quantified
	{
		private static final long serialVersionUID = 1L;
		public final int minRepetitions;
		public final Integer maxRepetitions;

		public RepeatRange(final Quantifiable base, final int min, final Integer max, final Mode mode)
		{
			super(
					base, 
					mode,
					LazyString.str("{")
						.append(Integer.toString(min))
						.append(",")
						.append(max == null ? "" : max.toString())
						.append("}")
						.append(mode.symbol)
				);
			this.minRepetitions = min;
			this.maxRepetitions = max;
		}

		@Override
		public Integer boundedLength() 
		{
			if (base.repetitionInvalidatesBounds())
				return null;
			final Integer baseLength = base.boundedLength();
			if (baseLength == null || maxRepetitions == null)
				return null;
			final long maximumLength = baseLength.longValue() * maxRepetitions.longValue();
			if (maximumLength <= 0xfffffffL) // arbitrary value from Pattern source code
				return (int)maximumLength;
			return null;
		}

		@Override
		public boolean repetitionInvalidatesBounds() 
		{
			return minRepetitions == 0 || base.repetitionInvalidatesBounds();
		}

		@Override
		public boolean possiblyZeroLength() 
		{
			return minRepetitions == 0 || base.possiblyZeroLength();
		}
	}
	
	@Deprecated
	public static RepeatRange repeat(final Quantifiable base, final int min, final int max)
	{
		return new RepeatRange(base, min, max, Mode.GREEDY); 
	}
	
	@Deprecated
	public static RepeatRange repeatReluctantly(final Quantifiable base, final int min, final int max)
	{
		return new RepeatRange(base, min, max, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static RepeatRange repeatPossessively(final Quantifiable base, final int min, final int max)
	{
		return new RepeatRange(base, min, max, Mode.POSSESSIVE);
	}
	
	@Deprecated
	public static RepeatRange atLeast(final Quantifiable base, final int n)
	{
		return new RepeatRange(base, n, null, Mode.GREEDY);
	}
	
	@Deprecated
	public static RepeatRange atLeastReluctantly(final Quantifiable base, final int n)
	{
		return new RepeatRange(base, n, null, Mode.RELUCTANT);
	}
	
	@Deprecated
	public static RepeatRange atLeastPossessively(final Quantifiable base, final int n)
	{
		return new RepeatRange(base, n, null, Mode.POSSESSIVE);
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
