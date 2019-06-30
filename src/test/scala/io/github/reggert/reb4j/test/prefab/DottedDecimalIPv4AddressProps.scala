package io.github.reggert.reb4j.test.prefab

import java.util.regex.Pattern

import io.github.reggert.reb4j._
import io.github.reggert.reb4j.prefab.DottedDecimalIPv4Address._
import org.junit.Test
import org.scalacheck.Prop._
import org.scalacheck.{Gen, Prop}
import org.scalatestplus.junit.JUnitSuite
import org.scalatestplus.scalacheck.Checkers

import scala.util.Try

class DottedDecimalIPv4AddressProps extends JUnitSuite with Checkers 
{
	
	val numericString: Pattern = """\d+""".r.pattern
	def isNumericString(s : String): Boolean = numericString.matcher(s).matches
	
	val genOctets: Gen[(Int, Int, Int, Int)] = for {
		a <- Gen.choose(0, 255)
		b <- Gen.choose(0, 255)
		c <- Gen.choose(0, 255)
		d <- Gen.choose(0, 255)
	} yield (a, b, c, d)

	object IntString {
		import java.lang.Integer.parseInt
		def unapply(s : String): Option[Int] = Try{parseInt(s)}.toOption
	}
	
	implicit final class ExpressionProps(val expr : Expression) {
		
		def validatesNumberInRange(min : Int, max : Int): Prop = forAll (Gen.choose(min, max)) { n : Int =>
			expr.toPattern.matcher(n.toString).matches
		}
		
		def invalidatesNumberOutOfRange(min : Int, max : Int): Prop = forAll { n : Int =>
			(n < min || n > max : Prop) ==> (!expr.toPattern.matcher(n.toString).matches)
		}
		
		val invalidatesNonNumericString: Prop = forAll { s : String =>
			(!isNumericString(s) : Prop) ==> (!expr.toPattern.matcher(s).matches)
		}
		
		def matchesOnlyNumberInRange(min : Int, max : Int): Prop =
			validatesNumberInRange(min, max) && invalidatesNumberOutOfRange(min, max) && invalidatesNonNumericString
		
		private val Extractor = expr.toPattern.pattern.r
		
		val validatesAddressAndCapturesOctets: Prop = forAll (genOctets) {
			case (a, b, c, d) => s"$a.$b.$c.$d" match
			{
				case Extractor(as, bs, cs, ds) 
					if (as, bs, cs, ds) == (a.toString, b.toString, c.toString, d.toString) => true
				case _ => false
			}
		}
	
		val invalidatesAddressWithBadOctet: Prop = forAll { (a : Int, b : Int, c : Int, d : Int) =>
			(List(a, b, c, d).exists(x => x < 0 || x > 255) : Prop) ==>
				(!expr.toPattern.matcher(s"$a.$b.$c.$d").matches)
		}
		
		val invalidatesNonAddress: Prop = forAll { s : String => s match {
			case Extractor(as, bs, cs, ds) => (as, bs, cs, ds) match {
				case _ if s != s"$as.$bs.$cs.$ds" => false
				case (IntString(a), IntString(b), IntString(c), IntString(d)) 
					if Traversable(a, b, c, d) forall {x => x >= 0 && x <= 255} => true
				case _ => false
			}
			case _ => true
		}}
			
	}
	
	
	@Test def oneDigitOctetMatchesPrecisely() : Unit = check(oneDigitOctet matchesOnlyNumberInRange(0, 9))
	@Test def twoDigitOctetMatchesPrecisely() : Unit = check(twoDigitOctet matchesOnlyNumberInRange(10, 99))
	@Test def oneHundredsOctetMatchesPrecisely() : Unit = check(oneHundredsOctet matchesOnlyNumberInRange(100, 199))
	@Test def lowTwoHundredsOctetMatchesPrecisely() : Unit = check(lowTwoHundredsOctet matchesOnlyNumberInRange(200, 249))
	@Test def highTwoHundredsOctetMatchesPrecisely() : Unit = check(highTwoHundredsOctet matchesOnlyNumberInRange(250, 255))
	@Test def octetMatchesPrecisely() : Unit = check(octet matchesOnlyNumberInRange(0, 255))
	@Test def dottedDecimalIPAddressMatchesPrecisely() : Unit = check(
			dottedDecimalIPAddress.validatesAddressAndCapturesOctets
			&& dottedDecimalIPAddress.invalidatesAddressWithBadOctet
			&& dottedDecimalIPAddress.invalidatesNonAddress,
			minSuccessful(100000), 
			maxDiscardedFactor(5.0)
		)
		
}