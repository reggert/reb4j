package io.github.reggert.reb4j;


/**
 * Extension to {@link AbstractAlternative} adding the
 * canonical implementation of {@link Sequenceable}.
 */
public abstract class AbstractSequenceableAlternative 
	extends AbstractAlternative
	implements Sequenceable
{
	private static final long serialVersionUID = 1L;

	@Override
	public final Sequence andThen(final Sequenceable right)
	{return new Sequence(this, right);}

	@Override
	public final Sequence andThen(final Sequence right)
	{return new Sequence(this, right);}
}
