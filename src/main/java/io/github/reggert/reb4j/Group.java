package io.github.reggert.reb4j;

import java.util.Arrays;

import fj.data.List;
import io.github.reggert.reb4j.data.Rope;

/**
 * Expression that has been grouped in parentheses.
 */
public abstract class Group extends AbstractQuantifiableSequenceableAlternative 
	implements Quantifiable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The expression that is enclosed in parentheses.
	 */
	public final Expression nested;
	
	private final Rope expression;

	private Group(final Expression nested, final Rope opening)
	{
		if (nested == null) throw new NullPointerException("nested");
		assert opening != null;
		this.nested = nested;
		this.expression = opening.append(nested.expression()).append(")");
	}
	
	@Override
	public Rope expression()
	{
		return expression;
	}
	
	@Override
	public final Integer boundedLength() 
	{
		return nested.boundedLength();
	}
	
	@Override 
	public final boolean repetitionInvalidatesBounds() 
	{
		return nested.repetitionInvalidatesBounds();
	}
			
	@Override 
	public final boolean possiblyZeroLength() 
	{
		return nested.possiblyZeroLength();
	}
	
	
	public final static class Capture extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(");
		
		public Capture(final Expression nested)
		{
			super(nested, OPENING);
		}
	}
	
	/**
	 * Constructs a capturing group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Capture capture(final Expression nested)
	{
		return new Capture(nested);
	}
	
	public final static class NonCapturing extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?:");
		
		public NonCapturing(final Expression nested)
		{
			super(nested, OPENING);
		}
	}
	
	/**
	 * Constructs a non-capturing group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static NonCapturing nonCapturing(final Expression nested)
	{
		return new NonCapturing(nested);
	}
	
	public final static class Independent extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?>");
		
		public Independent(final Expression nested)
		{
			super(nested, OPENING);
		}
	}
	
	/**
	 * Constructs an independent group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Independent independent(final Expression nested) 
	{
		return new Independent(nested);
	}
	
	public final static class PositiveLookAhead extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?=");
		
		public PositiveLookAhead(final Expression nested)
		{
			super(nested, OPENING);
		}
	}
	
	/**
	 * Constructs a group that uses positive look-ahead.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static PositiveLookAhead positiveLookAhead(final Expression nested) 
	{
		return new PositiveLookAhead(nested);
	}
	
	public final static class NegativeLookAhead extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?!");
		
		public NegativeLookAhead(final Expression nested)
		{
			super(nested, OPENING);
		}
	}
	
	/**
	 * Constructs a group that uses negative look-ahead.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static NegativeLookAhead negativeLookAhead(final Expression nested) 
	{
		return new NegativeLookAhead(nested);
	}
	
	public final static class PositiveLookBehind extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?<=");
		
		public PositiveLookBehind(final Expression nested) throws UnboundedLookBehindException
		{
			super(nested, OPENING);
			if (nested.boundedLength() == null)
				throw new UnboundedLookBehindException(nested);
		}
	}
	
	/**
	 * Constructs a group that uses positive look-behind.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws UnboundedLookBehindException 
	 *  if <var>nested</var> does not have a maximum length.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static PositiveLookBehind positiveLookBehind(final Expression nested) throws UnboundedLookBehindException 
	{
		return new PositiveLookBehind(nested);
	}
	
	public final static class NegativeLookBehind extends Group
	{
		private static final long serialVersionUID = 1L;
		private static final Rope OPENING = Rope.fromString("(?<!");
		
		public NegativeLookBehind(final Expression nested) throws UnboundedLookBehindException
		{
			super(nested, OPENING);
			if (nested.boundedLength() == null)
				throw new UnboundedLookBehindException(nested);
		}
	}
	
	/**
	 * Constructs a group that uses negative look-behind.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws UnboundedLookBehindException 
	 * 	if <var>nested</var> does not have a maximum length.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static NegativeLookBehind negativeLookBehind(final Expression nested) 
		throws UnboundedLookBehindException 
	{
		return new NegativeLookBehind(nested);
	}
	
	public final static class EnableFlags extends Group
	{
		private static final long serialVersionUID = 1L;
		public final List<Flag> flags;
		
		private static Rope opening(final Flag... flags)
		{
			return Rope.fromString("(?").append(Flag.toString(flags)).append(":");
		}
		
		public EnableFlags(final Expression nested, final Flag... flags)
		{
			super(nested, opening(flags));
			this.flags = List.iterableList(Arrays.asList(flags));
		}
	}
	
	/**
	 * Constructs a group that enables the specified matcher flags.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @param flags
	 *  the flags to enable.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static EnableFlags enableFlags(final Expression nested, final Flag... flags) 
	{
		return new EnableFlags(nested, flags);
	}
	
	public final static class DisableFlags extends Group
	{
		private static final long serialVersionUID = 1L;
		public final List<Flag> flags;
		
		private static Rope opening(final Flag... flags)
		{
			return Rope.fromString("(?-").append(Flag.toString(flags)).append(":");
		}
		
		public DisableFlags(final Expression nested, final Flag... flags)
		{
			super(nested, opening(flags));
			this.flags = List.iterableList(Arrays.asList(flags));
		}
	}
	/**
	 * Constructs a group that disables the specified matcher flags.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @param flags
	 *  the flags to disable.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static DisableFlags disableFlags(final Expression nested, final Flag... flags) 
	{
		return new DisableFlags(nested, flags);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + expression.hashCode();
		result = prime * result + nested.hashCode();
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
		final Group other = (Group) obj;
		return expression.equals(other.expression) && nested.equals(other.nested);
	}

}
