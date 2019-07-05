package io.github.reggert.reb4j.charclass;


import io.github.reggert.reb4j.data.Rope;


/**
 * Predefined character class referenced using an escape sequence.
 */
public class PredefinedClass extends CharClass
{
	private static final long serialVersionUID = 1L;
	public final char nameChar;
	
	protected final char invertedNameChar()
	{
		return Character.isUpperCase(nameChar) ? 
			Character.toLowerCase(nameChar) : Character.toUpperCase(nameChar); 
	}
	
	PredefinedClass(final char nameChar)
	{
		this.nameChar = nameChar;
	}

	@Override
	public PredefinedClass negated()
	{
		return new PredefinedClass(invertedNameChar());
	}

	@Override
	public Rope unitableForm()
	{
		return Rope.fromString("\\").append(Character.toString(nameChar));
	}

	@Override
	public final Rope independentForm()
	{return unitableForm();}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + nameChar;
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
		final PredefinedClass other = (PredefinedClass) obj;
		return nameChar == other.nameChar;
	}

}
