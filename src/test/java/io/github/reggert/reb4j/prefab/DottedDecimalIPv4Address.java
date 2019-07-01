package io.github.reggert.reb4j.prefab;

import io.github.reggert.reb4j.*;
import io.github.reggert.reb4j.charclass.CharClass;
import io.github.reggert.reb4j.charclass.CharClass.Perl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Matcher;

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

    /**
     * Parses the specified string as an IPv4 address in dotted-decimal notation.
     *
     * @param s the string to parse.
     * @return the parsed {@link Inet4Address}, or {@link Optional#empty()} if the string is invalid.
     */
    public static Optional<Inet4Address> parse(final String s) {
        final Matcher matcher = dottedDecimalIPAddress.toPattern().matcher(s);
        if (matcher.matches()) {
            final byte[] octets = new byte[]{
                (byte) Integer.parseInt(matcher.group(1)),
                (byte) Integer.parseInt(matcher.group(2)),
                (byte) Integer.parseInt(matcher.group(3)),
                (byte) Integer.parseInt(matcher.group(4))
            };
            try {
                return Optional.of((Inet4Address) InetAddress.getByAddress(s, octets));
            }
            catch (final UnknownHostException e) {
                throw new AssertionError("Unreachable", e);
            }
        }
        else {
            return Optional.empty();
        }
    }
}
