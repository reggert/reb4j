package io.github.reggert.reb4j;

import io.github.reggert.reb4j.Quantified.Mode;

/**
 * Interface implemented by expressions that may have quantifiers applied to
 * them without needing to first be wrapped in parentheses. 
 */
public interface Quantifiable extends Expression
{
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using the specified matching mode.
	 */
	Quantified anyTimes(final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using greedy matching.
	 */
	Quantified anyTimes();

	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 */
	@Deprecated
	Quantified anyTimesReluctantly();
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 */
	@Deprecated
	Quantified anyTimesPossessively();
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using the specified matching mode.
	 */
	Quantified atLeastOnce(final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 */
	Quantified atLeastOnce();
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 */
	@Deprecated
	Quantified atLeastOnceReluctantly();
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 */
	@Deprecated
	Quantified atLeastOncePossessively();
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using the specified matching mode.
	 */
	Quantified optional(final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 */
	Quantified optional();
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 */
	@Deprecated
	Quantified optionalReluctantly();
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 */
	@Deprecated
	Quantified optionalPossessively();
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using the specified matching mode.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @param mode
	 *  the matching mode to use; must not be null.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	Quantified repeat(final int n, final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using greedy matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	Quantified repeat(final int n);
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using reluctant matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	@Deprecated
	Quantified repeatReluctantly(final int n);
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using possessive matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	@Deprecated
	Quantified repeatPossessively(final int n);
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using the specified matching mode.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt;= <var>min</var>.
	 * @param mode the matching mode to use; must not be null.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	Quantified repeat(final int min, final int max, final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using greedy matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt;= <var>min</var>.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	Quantified repeat(final int min, final int max);
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using reluctant matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt; 0.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	@Deprecated
	Quantified repeatReluctantly(final int min, final int max);
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using possessive matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt; 0.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	@Deprecated
	Quantified repeatPossessively(final int min, final int max);
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using the specified matching mode.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @param mode the matching mode to use; must not be null.
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	Quantified atLeast(final int n, final Mode mode);
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using greedy matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	Quantified atLeast(final int n);
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using reluctant matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	@Deprecated
	Quantified atLeastReluctantly(final int n);
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using possessive matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	@Deprecated
	Quantified atLeastPossessively(final int n);
}
