package io.github.reggert.reb4j.prefab;

import io.github.reggert.reb4j.Alternation;
import io.github.reggert.reb4j.Alternative;
import io.github.reggert.reb4j.CharLiteral;
import io.github.reggert.reb4j.Group;
import io.github.reggert.reb4j.Literal;
import io.github.reggert.reb4j.Sequence;
import io.github.reggert.reb4j.charclass.CharClass;
import io.github.reggert.reb4j.charclass.CharClass.Perl;

public final class DottedDecimalIPv4Address
{
	private DottedDecimalIPv4Address() {}
	
	public static final Alternative oneDigitOctet = 
		Perl.DIGIT;
	public static final Alternative twoDigitOctet = 
		CharClass.range('1', '9').andThen(Perl.DIGIT);
	public static final Alternative oneHundredsOctet = 
		Literal.literal('1').andThen(Perl.DIGIT.repeat(2));
	public static final Alternative lowTwoHundredsOctet = Sequence.sequence(
			Literal.literal('2'),
			CharClass.range('0', '4'),
			Perl.DIGIT
		);
	public static final Alternative highTwoHundredsOctet = 
		Literal.literal("25").andThen(CharClass.range('0', '5'));
	public static final Alternation octet = Alternation.alternatives(
			oneDigitOctet, 
			twoDigitOctet, 
			oneHundredsOctet, 
			lowTwoHundredsOctet,
			highTwoHundredsOctet
		);
	public static final CharLiteral dot = Literal.literal('.');
	public static final Sequence dottedDecimalIPAddress = Sequence.sequence(
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet)
		);
}
