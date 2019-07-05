package io.github.reggert.reb4j.charclass;

import fj.data.Set;
import io.github.reggert.reb4j.Literal;
import io.github.reggert.reb4j.data.Rope;


/**
 * Character class consisting of multiple characters.
 */
public final class MultiChar extends CharClass
{
	private static final long serialVersionUID = 1L;
	public final Set<Character> characters;
	
	public MultiChar(final Set<Character> characters)
	{
		if (characters == null) throw new NullPointerException("characters");
		this.characters = characters;
	}

	@Override
	public Negated<MultiChar> negated()
	{
		return new Negated<>(this);
	}

	@Override
	public Rope unitableForm()
	{
		return characters.toStream().foldLeft(
			(a, b) -> a.append(Literal.escapeChar(b)),
				Rope.empty()
			);
	}

	@Override
	public Rope independentForm()
	{
		return Rope.fromString("[").append(unitableForm()).append("]");
	}
	
	/**
	 * Overloaded version of {@link CharClass#union(CharClass)} for arguments 
	 * of type {@link MultiChar} that simply merges the two objects into
	 * a single instance of {@link MultiChar}.
	 * 
	 * @param right another instance of {@link MultiChar}; must not be <code>null</code>.
	 * @return a new instance of {@link MultiChar}.
	 * @throws NullPointerException
	 * 	if the argument is <code>null</code>.
	 */
	public MultiChar union(final MultiChar right)
	{
		if (right == null) throw new NullPointerException("right == null");
		return new MultiChar(characters.union(right.characters));
	}
	
	/**
	 * Overloaded version of {@link CharClass#union(CharClass)} for arguments 
	 * of type {@link SingleChar} that simply merges the two objects into
	 * a single instance of {@link MultiChar}.
	 * 
	 * @param right another instance of {@link MultiChar}; must not be <code>null</code>.
	 * @return a new instance of {@link MultiChar}.
	 * @throws NullPointerException
	 * 	if the argument is <code>null</code>.
	 */
	public MultiChar union(final SingleChar right)
	{
		if (right == null) throw new NullPointerException("right == null");
		return new MultiChar(characters.insert(right.character));
	}

}
