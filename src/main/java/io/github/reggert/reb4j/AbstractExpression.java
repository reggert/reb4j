package io.github.reggert.reb4j;

import java.util.regex.Pattern;


/**
 * Base implementation of {@link Expression}.
 *
 * This class provides the canonical implementations of {@link #toString()}
 * and {@link #toPattern()}.
 */
public abstract class AbstractExpression implements Expression 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Always returns the same value as {@link io.github.reggert.reb4j.Expression#expression}.
	 */
	@Override
	public final String toString()
	{return expression().toStringEager();}

	@Override
	public final Pattern toPattern()
	{return Pattern.compile(toString());}

}
