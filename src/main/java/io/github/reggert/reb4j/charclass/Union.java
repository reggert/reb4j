package io.github.reggert.reb4j.charclass;

import fj.data.List;
import io.github.reggert.reb4j.data.Rope;


/**
 * Character class composed of the union of two other character classes.
 */
public final class Union extends CharClass
{
	private static final long serialVersionUID = 1L;
	public final List<CharClass> subsets;
	
	Union(final List<CharClass> subsets)
	{
		if (subsets == null) throw new NullPointerException("subsets");
		this.subsets = subsets;
	}

	@Override
	public Negated<Union> negated()
	{
		return new Negated<>(this);
	}

	@Override
	public Rope unitableForm()
	{
		return subsets.foldLeft(
			(a, b) -> a.append(b.unitableForm()),
				Rope.empty()
			);
	}

	@Override
	public Rope independentForm()
	{
		return Rope.fromString("[").append(unitableForm()).append("]");
	}

	@Override
	public Union union(final Union right)
	{
		return new Union(subsets.append(right.subsets));
	}

	@Override
	public Union union(final CharClass right)
	{
		return new Union(subsets.append(List.single(right)));
	}
	
	static Union union(final CharClass left, final Union right)
	{
		return new Union(right.subsets.cons(left));
	}
	
	static Union union(final CharClass left, final CharClass right)
	{
		return new Union(List.list(left, right));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + subsets.hashCode();
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
		final Union other = (Union) obj;
		return subsets.equals(other.subsets);
	}
	
}


