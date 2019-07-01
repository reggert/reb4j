package io.github.reggert.reb4j.prefab;

import io.github.reggert.reb4j.*;
import io.github.reggert.reb4j.charclass.CharClass;
import io.github.reggert.reb4j.charclass.CharClass.Perl;

public final class DottedDecimalIPv4Address {
    private DottedDecimalIPv4Address() {
    }

    public static final Alternative oneDigitOctet =
        Perl.DIGIT;
    public static final Alternative twoDigitOctet =
        CharClass.range('1', '9').andThen(Perl.DIGIT);
    public static final Alternation threeDigitOctet = Alternation.alternatives(
        Literal.literal('1').andThen(Perl.DIGIT.repeat(2)),
        Sequence.sequence(
            Literal.literal('2'),
            CharClass.range('0', '4'),
            Perl.DIGIT
        ),
        Literal.literal("25").andThen(CharClass.range('0', '5'))
    );
    public static final Alternation octet = Alternation.alternatives(
        oneDigitOctet,
        twoDigitOctet,
        threeDigitOctet
    );
    private static final CharLiteral dot = Literal.literal('.');
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
