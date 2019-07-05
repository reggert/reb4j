package io.github.reggert.reb4j;

import fj.Ord;
import fj.data.List;
import fj.data.Set;
import fj.data.TreeMap;
import io.github.reggert.reb4j.data.Rope;


/**
 * Expression that exactly matches a specific string or character.
 */
public abstract class Literal extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;

	/**
	 * The set of characters that must be escaped in expressions.
	 */
	public static final Set<Character> NEEDS_ESCAPE = 
		Set.set(
				Ord.charOrd, 
				List.fromString("()[]{}.,-\\|+*?$^&:!<>=#").array(Character[].class)
			);
	
	/**
	 * Characters that get their own special escape sequences.
	 */
	public static final TreeMap<Character, String> specialEscapes =
		TreeMap.<Character, String>empty(Ord.charOrd)
			.set('\n', "\\n")
			.set('\t', "\\t")
			.set('\r', "\\r")
			.set('\f', "\\f")
			.set('\u0007', "\\a")
			.set('\u001b', "\\e")
			.set('\0', "\\00");
	
	/**
	 * Helper function that escapes the specified character.
	 * 
	 * @param c the character to escape; must not be <code>null</code>.
	 * @return a {@link Rope} containing the escaped (if necessary) character.
	 * @throws NullPointerException
	 * 	if <var>c</var> is <code>null</code>. 
	 */
	public static Rope escapeChar(final Character c)
	{
		return NEEDS_ESCAPE.member(c) ? 
			Rope.fromString("\\").append(c.toString())
			: Rope.fromString(specialEscapes.get(c).orSome(c.toString()));
	}
	
	/**
	 * Helper function that escapes the specified string.
	 * 
	 * @param unescaped
	 * 	the string to escape; must not be <code>null</code>.
	 * @return the escaped string.
	 * @throws NullPointerException
	 * 	if <var>unescaped</var> is <code>null</code>.
	 */
	public static String escape(final String unescaped)
	{
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < unescaped.length(); i++) {
			builder.append(escapeChar(unescaped.charAt(i)));
		}
		return builder.toString();
	}
	
	/**
	 * Constructs a new {@link StringLiteral} from the specified string, 
	 * escaping it as necessary.
	 * 
	 * @param unescaped
	 * 	the unescaped string to represent as a {@link StringLiteral};
	 * 	must not be null.
	 * @return a new instance of {@link StringLiteral}.
	 * @throws NullPointerException
	 * 	if <var>unescaped</var> is <code>null</code>.
	 */
	public static StringLiteral literal(final String unescaped)
	{return new StringLiteral(unescaped);}
	
	/**
	 * Constructs a new {@link CharLiteral} from the specified character,
	 * escaping it as necessary.
	 * 
	 * @param unescapedChar
	 * 	the unescaped character to represent as a {@link CharLiteral}.
	 * @return a new instance fo {@link CharLiteral}.
	 */
	public static CharLiteral literal(final char unescapedChar)
	{return new CharLiteral(unescapedChar);}
	
	Literal()
	{}
	
	/**
	 * Returns the original unescaped string (or character as a string) 
	 * matched by this expression.
	 */
	public abstract String unescaped();

	/**
	 * Returns the escaped form of the string or character matched by
	 * this expression.
	 */
	public final String escaped()
	{
		return escape(unescaped());
	}
	
	@Override
	public final Rope expression()
	{
		return Rope.fromString(escaped());
	}
	
	@Override
	public final Integer boundedLength() 
	{
		return unescaped().length();
	}
			
	@Override
	public final boolean repetitionInvalidatesBounds()
	{
		return possiblyZeroLength();
	}
			
	@Override		
	public final boolean possiblyZeroLength()
	{
		return unescaped().isEmpty();
	}
}
