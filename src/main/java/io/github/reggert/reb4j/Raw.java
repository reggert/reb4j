package io.github.reggert.reb4j;

import fj.data.List;
import io.github.reggert.reb4j.data.Rope;


/**
 * Base class for expressions that are readily combined with other similar expressions.
 * 
 * This encompasses special predefined expressions, processed literals, 
 * and combinations thereof. 
 * This class mainly exists so that long sequences of such expressions can be flattened rather
 * than causing the construction of deep trees.
 */
public abstract class Raw extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	private final Rope rawExpression;
	
	Raw(final Rope rawExpression)
	{
		assert rawExpression != null;
		this.rawExpression = rawExpression;
	}

	@Override
	public final Rope expression()
	{return rawExpression;}
	

	/**
	 * Overloaded version of {@link Sequenceable#andThen(Sequenceable)}
	 * for when the argument is an instance of {@link Raw}.
	 * 
	 * @param right an instance of {@link Raw}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound andThen(final Raw right)
	{
		if (right == null) throw new NullPointerException("right");
		return new Compound(List.list(this, right));
	}

	/**
	 * Overloaded version of {@link Sequenceable#andThen(Sequenceable)} )}
	 * for when the argument is an instance of {@link Compound}.
	 * 
	 * @param right an instance of {@link Compound}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound andThen(final Compound right)
	{
		if (right == null) throw new NullPointerException("right");
		return new Compound(right.components.cons(this));
	}

	/**
	 * Overloaded version of {@link Sequenceable#andThen(Sequenceable)} )}
	 * for when the argument is an instance of {@link Literal}.
	 * 
	 * @param right an instance of {@link Literal}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound andThen(final Literal right)
	{
		if (right == null) throw new NullPointerException("right");
		return this.andThen(new EscapedLiteral(right));
	}
	

	/**
	 * Expression consisting of multiple 
	 */
	public static final class Compound extends Raw
	{
		private static final long serialVersionUID = 1L;
		public final List<Raw> components;
		
		private Compound(final List<Raw> components)
		{
			super(compoundExpression(components));
			this.components = components;
		}
		
		private static Rope compoundExpression(final List<Raw> components)
		{
			return components.foldLeft(
				(a, b) -> a.append(b.rawExpression),
					Rope.empty()
				);
		}
		
		@Override
		public Compound andThen(final Raw right)
		{
			return new Compound(components.append(List.single(right)));
		}
		
		@Override
		public Compound andThen(final Compound right)
		{
			return new Compound(components.append(right.components));
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
			return components.forall(Expression::repetitionInvalidatesBounds);
		}
		
		@Override 
		public boolean possiblyZeroLength()
		{
			return components.forall(Expression::possiblyZeroLength);
		}
	}
	
	
	/**
	 * Adapter from {@link Literal} to {@link Raw} to facilitate
	 * merging literals.
	 */
	public static final class EscapedLiteral extends Raw
	{
		private static final long serialVersionUID = 1L;
		public final Literal literal;
		
		public EscapedLiteral(final Literal literal)
		{
			super(Rope.fromString(literal.escaped()));
			this.literal = literal;
		}

		@Override
		public Integer boundedLength() 
		{
			return literal.boundedLength();
		}
		
		@Override 
		public boolean repetitionInvalidatesBounds() 
		{
			return possiblyZeroLength();
		}
		
		@Override 
		public boolean possiblyZeroLength()
		{
			return literal.possiblyZeroLength();
		}
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + rawExpression.hashCode();
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
		final Raw other = (Raw) obj;
		return rawExpression.equals(other.rawExpression);
	}
	
}



