package io.github.reggert.reb4j.charclass;

import fj.data.List;
import io.github.reggert.reb4j.data.Rope;


/**
 * Character class consisting of the intersection of two other character classes.
 */
public final class Intersection extends CharClass
{
	private static final long serialVersionUID = 1L;
	
	public final List<CharClass> supersets;
	
	private Intersection(final List<CharClass> supersets)
	{
		if (supersets == null) throw new NullPointerException("supersets");
		this.supersets = supersets;
	}

	@Override
	public Negated<Intersection> negated()
	{
		return new Negated<>(this);
	}

	@Override
	public Rope unitableForm()
	{
		return supersets.tail().foldLeft(
			(a, b) -> a.append("&&").append(b.independentForm()),
				supersets.head().independentForm()
			);
	}

	@Override
	public Rope independentForm()
	{
		return Rope.fromString("[").append(unitableForm()).append("]");
	}
	
	@Override
	public Intersection intersect(final CharClass right)
	{
		return new Intersection(supersets.append(List.single(right))); 
	}

	@Override
	public Intersection intersect(final Intersection right)
	{
		return new Intersection(supersets.append(right.supersets));
	}
	
	static Intersection intersect(final CharClass left, final CharClass right)
	{
		return new Intersection(List.list(left, right));
	}
	
	static Intersection intersect(final CharClass left, final Intersection right)
	{
		return new Intersection(right.supersets.cons(left));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + supersets.hashCode();
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
		final Intersection other = (Intersection) obj;
		return supersets.equals(other.supersets);
	}
}

