package io.github.reggert.reb4j.charclass;

import static fj.Ord.charOrd;

import io.github.reggert.reb4j.AbstractQuantifiableSequenceableAlternative;
import io.github.reggert.reb4j.Alternative;
import io.github.reggert.reb4j.Expression;
import io.github.reggert.reb4j.Quantifiable;
import io.github.reggert.reb4j.Sequenceable;

import java.lang.Character.UnicodeBlock;


import fj.data.Set;
import io.github.reggert.reb4j.data.Rope;

/**
 * Base class representing an expression that matches a single character 
 * within a class of characters.
 */
public abstract class CharClass extends AbstractQuantifiableSequenceableAlternative
	implements Expression, Quantifiable, Sequenceable, Alternative
{
	private static final long serialVersionUID = 1L;

	/**
	 * The regular expression string that can be used within square brackets
	 * to merge with other character classes.
	 */
	protected abstract Rope unitableForm();
	
	/**
	 * The regular expression string that can be used independently of square 
	 * brackets.
	 */
	protected abstract Rope independentForm();
	
	@Override
	public final Rope expression()
	{return independentForm();}
	
	@Override
	public Integer boundedLength() 
	{
		return 1;
	}

	@Override
	public boolean repetitionInvalidatesBounds() 
	{
		return false;
	}

	@Override
	public boolean possiblyZeroLength() 
	{
		return false;
	}

	/**
	 * Returns an expressing matching a single character that is not within
	 * the class of characters matched by this expression.
	 */
	public abstract CharClass negated();
	
	/**
	 * Returns the union of this character class with the specified
	 * character classes.
	 */
	public Union union(final Union right)
	{return Union.union(this, right);}

	/**
	 * Returns the union of this character class with the specified
	 * character class.
	 */
	public Union union(final CharClass right)
	{return Union.union(this, right);}
	
	/**
	 * Returns the intersection of this character class with the 
	 * specified character class.
	 */
	public Intersection intersect(final CharClass right)
	{return Intersection.intersect(this, right);}

	/**
	 * Returns the intersection of this character class with the 
	 * specified character classes.
	 */
	public Intersection intersect(final Intersection right)
	{return Intersection.intersect(this, right);}
	
	/**
	 * Constructs a character class consisting of a single character.
	 */
	public static SingleChar character(final char c)
	{return new SingleChar(c);}
	
	/**
	 * Constructs a character class consisting of several characters.
	 */
	public static MultiChar characters(final char c1, final char c2, final char... cs)
	{
		Set<Character> set = Set.set(charOrd, c1, c2);
		for (final char c : cs)
			set = set.insert(c);
		return new MultiChar(set);
	}
	
	/**
	 * Constructs a character class consisting of all characters in
	 * a range.
	 * 
	 * @param first
	 * 	the minimum bound for the range.
	 * @param last
	 * 	the maximum bound for the range; must be greater than <var>first</var>.
	 * @throws IllegalArgumentException
	 * 	if <var>last</var> &lt;= <var>first</var>.
	 */
	public static CharRange range(final char first, final char last)
	{return new CharRange(first, last);}
	
	/**
	 * Module containing Perl-style predefined character classes.
	 */
	public static abstract class Perl
	{
		private Perl() {}
		
		/**
		 * Perl-style character class that matches a single decimal digit.
		 */
		public static final PredefinedClass DIGIT = new PredefinedClass('d');
		
		/**
		 * Perl-style character class that matches whitespace.
		 */
		public static final PredefinedClass SPACE = new PredefinedClass('s');
		
		/**
		 * Perl-style character class that matches "word" characters.
		 */
		public static final PredefinedClass WORD = new PredefinedClass('w');
	}
	
	/**
	 * Module containing POSIX-style predefined character classes.
	 */
	public static abstract class Posix
	{
		private Posix() {}
		
		/**
		 * POSIX-style character class that matches a lowercase letter.
		 */
		public static final NamedPredefinedClass LOWER = new NamedPredefinedClass("Lower");
		
		/**
		 * POSIX-style character class that matches an uppercase letter.
		 */
		public static final NamedPredefinedClass UPPER = new NamedPredefinedClass("Upper");
		
		/**
		 * POSIX-style character class that matches an alphabetical letter.
		 */
		public static final NamedPredefinedClass ALPHA = new NamedPredefinedClass("Alpha");
		
		/**
		 * POSIX-style character class that matches a decimal digit.
		 */
		public static final NamedPredefinedClass DIGIT = new NamedPredefinedClass("Digit");
		
		/**
		 * POSIX-style character class that matches letters or digits.
		 */
		public static final NamedPredefinedClass ALNUM = new NamedPredefinedClass("Alnum");
		
		/**
		 * POSIX-style character class that matches a punctuation character.
		 */
		public static final NamedPredefinedClass PUNCT = new NamedPredefinedClass("Punct");
		
		/**
		 * POSIX-style character class that matches a graphical character.
		 */
		public static final NamedPredefinedClass GRAPH = new NamedPredefinedClass("Graph");
		
		/**
		 * POSIX-style character class that matches any printable character.
		 */
		public static final NamedPredefinedClass PRINT = new NamedPredefinedClass("Print");
		
		/**
		 * POSIX-style character class that matches a space or tab.
		 */
		public static final NamedPredefinedClass BLANK = new NamedPredefinedClass("Blank");
		
		/**
		 * POSIX-style character class that matches a control character.
		 */
		public static final NamedPredefinedClass CONTROL = new NamedPredefinedClass("Cntrl");
		
		/**
		 * POSIX-style character class that matches a hexadecimal digit.
		 */
		public static final NamedPredefinedClass HEX_DIGIT = new NamedPredefinedClass("XDigit");
		
		/**
		 * POSIX-style character class that matches any whitespace character.
		 */
		public static final NamedPredefinedClass SPACE = new NamedPredefinedClass("Space");
	}
	
	/**
	 * Module containing predefined character classes matching character traits
	 * defined by the {@link java.lang.Character} class.
	 */
	public static abstract class Java
	{
		private Java() {}
		
		/**
		 * Matches any single lowercase letter.
		 */
		public static final NamedPredefinedClass LOWER_CASE = new NamedPredefinedClass("javaLowerCase");
		
		/**
		 * Matches any single uppercase letter.
		 */
		public static final NamedPredefinedClass UPPER_CASE = new NamedPredefinedClass("javaUpperCase");
		
		/**
		 * Matches any single whitespace character.
		 */
		public static final NamedPredefinedClass WHITESPACE = new NamedPredefinedClass("javaWhitespace");
		
		/**
		 * Matches any single "mirrored" character.
		 */
		public static final NamedPredefinedClass MIRROR = new NamedPredefinedClass("javaMirrored");
	}
	
	/**
	 * Module containing predefined character classes matching a single
	 * character that has certain traits defined by the Unicode specification.
	 */
	public static abstract class Unicode
	{
		private Unicode() {}
		private static NamedPredefinedClass z (final String className) 
		{return new NamedPredefinedClass(className);}
		
		/**
		 * Creates a character class matching any single character within the
		 * specified Unicode block.
		 */
		public static NamedPredefinedClass block(final UnicodeBlock unicodeBlock)
		{return z("In" + unicodeBlock.toString());}
		
		/*
			From the Unicode Specification, version 4.0.0, the Unicode categories are:
				Lu = Letter, uppercase
				Ll = Letter, lowercase
				Lt = Letter, titlecase
				Lm = Letter, modifier
				Lo = Letter, other
				Mn = Mark, nonspacing
				Mc = Mark, spacing combining
				Me = Mark, enclosing
				Nd = Number, decimal digit
				Nl = Number, letter
				No = Number, other
				Zs = Separator, space
				Zl = Separator, line
				Zp = Separator, paragraph
				Cc = Other, control
				Cf = Other, format
				Cs = Other, surrogate
				Co = Other, private use
				Cn = Other, not assigned (including noncharacters)
				Pc = Punctuation, connector
				Pd = Punctuation, dash
				Ps = Punctuation, open
				Pe = Punctuation, close
				Pi = Punctuation, initial quote (may behave like Ps or Pe depending on usage)
				Pf = Punctuation, final quote (may behave like Ps or Pe depending on usage)
				Po = Punctuation, other
				Sm = Symbol, math
				Sc = Symbol, currency
				Sk = Symbol, modifier
				So = Symbol, other
		 */
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "letter" by the Unicode specification.
		 */
		public static abstract class Letter
		{
			private Letter() {}
			
			/**
			 * Matches uppercase letters.
			 */
			public static final NamedPredefinedClass UPPER_CASE = z("Lu");
			
			/**
			 * Matches lowercase letters.
			 */
			public static final NamedPredefinedClass LOWER_CASE = z("Ll");
			
			/**
			 * Matches titlecase letters.
			 */
			public static final NamedPredefinedClass TITLE_CASE = z("Lt");
			
			/**
			 * Matches letter modifiers.
			 */
			public static final NamedPredefinedClass MODIFIER = z("Lm");
			
			/**
			 * Matches "other" letters defined by Unicode.
			 */
			public static final NamedPredefinedClass OTHER = z("Lo");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "mark" by the Unicode specification.
		 */
		public static abstract class Mark
		{
			private Mark() {}
			
			/**
			 * Matches nonspacing marks.
			 */
			public static final NamedPredefinedClass NONSPACING = z("Mn");
			
			/**
			 * Matches spacing-combining marks.
			 */
			public static final NamedPredefinedClass SPACING_COMBINING = z("Mc");
			
			/**
			 * Matches enclosing marks.
			 */
			public static final NamedPredefinedClass ENCLOSING = z("Me");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "number" by the Unicode specification.
		 */
		public static abstract class Number
		{
			private Number() {}
			
			/**
			 * Matches decimal digits.
			 */
			public static final NamedPredefinedClass DECIMAL_DIGIT = z("Nd");
			
			/**
			 * Matches letter characters used as digits.
			 */
			public static final NamedPredefinedClass LETTER = z("Nl");
			
			/**
			 * Matches "other" digit characters defined by Unicode.
			 */
			public static final NamedPredefinedClass OTHER = z("No");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "separator" by the Unicode specification.
		 */
		public static abstract class Separator
		{
			private Separator() {}
			
			/**
			 * Mataches spaces.
			 */
			public static final NamedPredefinedClass SPACE = z("Zs");
			
			/**
			 * Matches line breaks.
			 */
			public static final NamedPredefinedClass LINE = z("Zl");
			
			/**
			 * Matches paragraph breaks.
			 */
			public static final NamedPredefinedClass PARAGRAPH = z("Zp");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that does not fit into any other category defined by the
		 * Unicode specification.
		 */
		public static abstract class Other
		{
			private Other() {}
			
			/**
			 * Matches control characters.
			 */
			public static final NamedPredefinedClass CONTROL = z("Cc");
			
			/**
			 * Matches formatting characters.
			 */
			public static final NamedPredefinedClass FORMAT = z("Cf");
			
			/**
			 * Matches surrogate characters.
			 */
			public static final NamedPredefinedClass SURROGATE = z("Cs");
			
			/**
			 * Matches characters defined for private use.
			 */
			public static final NamedPredefinedClass PRIVATE_USE = z("Co");
			
			/**
			 * Matches unassigned characters.
			 */
			public static final NamedPredefinedClass NOT_ASSIGNED = z("Cn");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that is defined as "punctuation" by the Unicode 
		 * specification.
		 */
		public static abstract class Punctuation
		{
			/**
			 * Matches connectors.
			 */
			public static final NamedPredefinedClass CONNECTOR = z("Pc");
			
			/**
			 * Matches dashes.
			 */
			public static final NamedPredefinedClass DASH = z("Pd");
			
			/**
			 * Matches "opening" punctuation.
			 */
			public static final NamedPredefinedClass OPEN = z("Po");
			
			/**
			 * Matches "closing" punctuation.
			 */
			public static final NamedPredefinedClass CLOSE = z("Pe");
			
			/**
			 * Matches initial quotes.
			 */
			public static final NamedPredefinedClass INITIAL_QUOTE = z("Pi");
			
			/**
			 * Matches closing quotes.
			 */
			public static final NamedPredefinedClass FINAL_QUOTE = z("Pf");
			
			/**
			 * Matches other punctuation.
			 */
			public static final NamedPredefinedClass OTHER = z("Po");
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that is defined as a "symbol" by the Unicode 
		 * specification.
		 */
		public static abstract class Symbol
		{
			/**
			 * Matches mathematical symbols.
			 */
			public static final NamedPredefinedClass MATH = z("Sm");
			
			/**
			 * Matches currency symbols.
			 */
			public static final NamedPredefinedClass CURRENCY = z("Sc");
			
			/**
			 * Matches symbol modifiers.
			 */
			public static final NamedPredefinedClass MODIFIER = z("Sk");
			
			/**
			 * Matches other symbols.
			 */
			public static final NamedPredefinedClass OTHER = z("So");
		}
	}
}
