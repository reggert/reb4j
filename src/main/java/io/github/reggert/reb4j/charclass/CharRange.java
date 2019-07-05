package io.github.reggert.reb4j.charclass;

import io.github.reggert.reb4j.Literal;
import io.github.reggert.reb4j.data.Rope;


/**
 * Character class consisting of a range of characters.
 */
public final class CharRange extends CharClass
{
	private static final long serialVersionUID = 1L;
	public final char first, last;
	
	CharRange(final char first, final char last)
	{
		if (first > last) throw new IllegalArgumentException("first must be <= last");
		this.first = first;
		this.last = last;
	}

	@Override
	public Negated<CharRange> negated()
	{
		return new Negated<>(this);
	}

	@Override
	public Rope unitableForm()
	{
		return Literal.escapeChar(first).append("-").append(Literal.escapeChar(last));
	}

	@Override
	public Rope independentForm()
	{
		return Rope.fromString("[").append(unitableForm()).append("]");
	}

}
