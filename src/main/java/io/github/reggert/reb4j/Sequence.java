package io.github.reggert.reb4j;

import fj.data.List;
import io.github.reggert.reb4j.data.Rope;

/**
 * Expression consisting of several sub-expressions that must be matched
 * in series.
 */
public final class Sequence extends AbstractExpression
	implements Alternative, Sequenceable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The sub-expressions that make up the sequence.
	 */
	public final List<Sequenceable> components;
	
	private Sequence(final List<Sequenceable> components)
	{
		if (components == null) throw new NullPointerException("components");
		this.components = components;
	}
	
	Sequence(final Sequence left, final Sequence right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = left.components.append(right.components);
	}
	
	Sequence(final Sequence left, final Sequenceable right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = left.components.append(List.single(right));
	}
	
	Sequence(final Sequenceable left, final Sequence right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = right.components.cons(left);
	}
	
	Sequence(final Sequenceable left, final Sequenceable right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = List.list(left, right);
	}
	
	/**
	 * Factory method to create a sequence from many sub-expressions.
	 * @param first
	 * 	the first sub-expression; must not be <code>null</code>.
	 * @param second
	 * 	the second sub-expression; must not be <code>null</code>.
	 * @param rest
	 * 	the remaining sub-expressions; must not be <code>null</code>.
	 * @return a new sequence.
	 * @throws NullPointerException
	 * 	if any argument is <code>null</code>.
	 */
	public static Sequence sequence(
			final Sequenceable first, 
			final Sequenceable second, 
			final Sequenceable... rest
		)
	{
		if (first == null) throw new NullPointerException("first");
		if (second == null) throw new NullPointerException("second");
		if (rest == null) throw new NullPointerException("rest");
		return new Sequence(List.list(rest).cons(second).cons(first));
	}
	

	@Override
	public Rope expression()
	{
		return components.foldLeft(
			(a, b) -> a.append(b.expression()),
				Rope.empty()
			);
	}

	@Override
	public Alternation or(final Alternation right) 
	{return new Alternation(this, right);}

	@Override
	public Alternation or(final Alternative right) 
	{return new Alternation(this, right);}
	
	@Override
	public Sequence andThen(final Sequenceable right)
	{return new Sequence(this, right);}

	@Override
	public Sequence andThen(final Sequence right)
	{return new Sequence(this, right);}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + components.hashCode();
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
		final Sequence other = (Sequence) obj;
		return components.equals(other.components);
	}

	@Override
	public Integer boundedLength() 
	{
		final Long maximumLength = components.foldLeft(
			(a, b) -> {
				if (a == null)
					return null;
				final Integer next = b.boundedLength();
				if (next == null)
					return null;
				return a + next;
			},
				0L
			);
		if (maximumLength == null || maximumLength > 0xfffffffL) // arbitrary large value that appears in Pattern source code.
			return null;
		return maximumLength.intValue();
	}
	
	@Override 
	public boolean repetitionInvalidatesBounds() 
	{
		return components.exists(Expression::repetitionInvalidatesBounds);
	}
	
	@Override 
	public boolean possiblyZeroLength()
	{
		return components.forall(Expression::possiblyZeroLength);
	}
}

