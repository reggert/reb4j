package io.github.reggert.reb4j;

import java.io.Serializable;
import java.util.regex.Pattern;

import fj.data.LazyString;

/**
 * Basic abstraction of a regular expression.
 * 
 * All implementations of this can be converted directly into a
 * {@link Pattern} object without risk of throwing an exception.
 * 
 * This interface is not intended to be implemented by clients,
 * and doing so will result in unpredictable behavior.
 */
public interface Expression extends Serializable 
{
	/**
	 * Returns the regular expression represented by this object.
	 */
	LazyString expression();
	
	/**
	 * Passes the regular expression represented by this object to 
	 * {@link java.util.regex.Pattern} and returns the result.
	 */
	Pattern toPattern();
	
	/**
	 * Indicates the computed maximum length of the expression, if one can be 
	 * determined.
	 * 
	 * @return the computed maximum length, or null if it is unbounded.
	 */
	Integer boundedLength();
}
