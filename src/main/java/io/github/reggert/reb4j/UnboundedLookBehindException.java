package io.github.reggert.reb4j;

public final class UnboundedLookBehindException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private final Expression unboundedExpression;
	
	public UnboundedLookBehindException(final Expression unboundedExpression)
	{
		super("Look-behind cannot be applied to the following expression because its size is potentially unbounded: " + unboundedExpression);
		this.unboundedExpression = unboundedExpression;
	}

	public Expression getUnboundedExpression() 
	{
		return unboundedExpression;
	}
}
