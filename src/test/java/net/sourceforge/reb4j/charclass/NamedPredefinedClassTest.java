package net.sourceforge.reb4j.charclass;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;


import org.junit.Test;

import com.github.reggert.reb4j.Entity;
import com.github.reggert.reb4j.Expression;
import com.github.reggert.reb4j.charclass.CharClass;

public class NamedPredefinedClassTest
{

	@Test
	public void testHashCodeAndEquals()
	{
		final HashSet<Expression> set = new HashSet<Expression>();
		set.add(CharClass.Posix.ALPHA);
		set.add(CharClass.Posix.ALPHA.negated().negated());
		set.add(CharClass.Posix.BLANK);
		assertThat(set.size(), is(2));
		assertThat(set.contains(CharClass.Posix.ALPHA), is(true));
		assertThat(set.contains(CharClass.Posix.CONTROL), is(false));
		assertThat(CharClass.Posix.ALPHA.equals(CharClass.Posix.ALPHA), is(true));
		assertThat(CharClass.Posix.ALPHA.equals(CharClass.Posix.ALPHA.negated().negated()), is(true));
		assertThat(CharClass.Posix.ALPHA.equals(CharClass.Posix.BLANK), is(false));
		assertThat(CharClass.Posix.ALPHA.equals(Entity.ANY_CHAR), is(false));
		assertThat(CharClass.Posix.ALPHA.equals(null), is(false));
	}

}
