package io.github.reggert.reb4j;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;
import io.github.reggert.reb4j.data.Rope;

/**
 * Expression representing a set of alternatives that may be matched.
 */
public final class Alternation extends AbstractExpression
	implements Alternative
{
	private static final long serialVersionUID = 1L;
	public final List<Alternative> alternatives;
	
	private Alternation(final List<Alternative> alternatives)
	{
		if (alternatives == null) throw new NullPointerException("alternatives");
		this.alternatives = alternatives;
	}
	
	/**
	 * Constructs a new alternation representing the union of two existing
	 * alternations.
	 * 
	 * @param left
	 * 	an alternation; must not be <code>null</code>.
	 * @param right
	 *  an alternation; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is <code>null</code>.
	 */
	Alternation(final Alternation left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(right.alternatives);
	}
	
	/**
	 * Constructs a new alternation representing the union of an existing
	 * alternation and an alternative to be appended to the end.
	 * 
	 * @param left
	 * 	an alternation; must not be <code>null</code>.
	 * @param right
	 *  an alternative to be appended; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is <code>null</code>.
	 */
	Alternation(final Alternation left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(List.single(right));
	}
	
	/**
	 * Constructs a new alternation representing the union of an existing
	 * alternation and an alternative to be prepended to the beginning.
	 * 
	 * @param left
	 * 	an alternative to be prepended; must not be <code>null</code>.
	 * @param right
	 *  an alternation; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is <code>null</code>.
	 */
	Alternation(final Alternative left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = right.alternatives.cons(left);
	}
	
	/**
	 * Constructs a new alternation that may match either of two alternatives.
	 * 
	 * @param left
	 * 	the first alternative; must not be <code>null</code>.
	 * @param right
	 *  the second alternative; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is <code>null</code>.
	 */
	Alternation(final Alternative left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = List.list(left, right);
	}
	
	/**
	 * Constructs a new alternation that may match any of several alternatives.
	 * 
	 * @param first
	 * 	the first alternative; must not <code>null</code>.
	 * @param second
	 * 	the second alternative; must not <code>null</code>.
	 * @param rest
	 * 	the remaining alternatives; must not <code>null</code>.
	 * @return a new Alternation.
	 * @throws NullPointerException
	 * 	if any argument is <code>null</code>.
	 */
	public static Alternation alternatives(
			final Alternative first, 
			final Alternative second, 
			final Alternative... rest
		)
	{
		if (first == null) throw new NullPointerException("first");
		if (second == null) throw new NullPointerException("second");
		if (rest == null) throw new NullPointerException("rest");
		return new Alternation(List.list(rest).cons(second).cons(first));
	}
	
	@Override
	public Rope expression()
	{
		return alternatives.tail().foldLeft(
				new F2<Rope, Alternative, Rope>()
				{
					@Override
					public Rope f(final Rope a, final Alternative b)
					{return a.append("|").append(b.expression());}
				}, 
				alternatives.head().expression()
			);
	}

	@Override
	public Alternation or(final Alternation right) 
	{return new Alternation(this, right);}

	@Override
	public Alternation or(final Alternative right) 
	{return new Alternation(this, right);}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + alternatives.hashCode();
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
		final Alternation other = (Alternation) obj;
		return alternatives.equals(other.alternatives);
	}

	@Override
	public Integer boundedLength() 
	{
		int maximumLength = 0;
		for (final Alternative alternative : alternatives)
		{
			final Integer alternativeLength = alternative.boundedLength();
			if (alternativeLength == null)
				return null;
			if (alternativeLength > maximumLength)
				maximumLength = alternativeLength;
		}
		return maximumLength;
	}

	@Override
	public boolean repetitionInvalidatesBounds() 
	{
		return true;
	}

	@Override
	public boolean possiblyZeroLength() 
	{
		for (final Alternative a : alternatives)
			if (a.possiblyZeroLength())
				return true;
		return false;
	}
}

