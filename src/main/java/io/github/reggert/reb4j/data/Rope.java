package io.github.reggert.reb4j.data;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;


/**
 * Rope implementation to avoid unnecessary copying of strings.
 */
@Immutable
public abstract class Rope implements Serializable, CharSequence {
    private static final long serialVersionUID = 1L;
    private static final Rope EMPTY = new Rope() {
        private static final long serialVersionUID = 1L;

        @Override
        public int length() {
            return 0;
        }

        @Override
        protected char charAtUnchecked(int index) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }

        @Nonnull
        @Override
        public String toString() {
            return "";
        }

        @Override
        protected void addTo(@Nonnull StringBuilder stringBuilder) {
            // nothing to add
        }
    };


    private Rope() {
    }


    /**
     * Creates a {@code Rope} from the specified {@code String}.
     *
     * @param stringValue the string to wrap.
     * @return a {@code Rope}.
     */
    @Nonnull
    public static Rope fromString(@Nonnull final String stringValue) {
        requireNonNull(stringValue, "stringValue");
        return new Rope() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void addTo(@Nonnull StringBuilder stringBuilder) {
                stringBuilder.append(stringValue);
            }

            @Override
            protected char charAtUnchecked(int index) {
                return stringValue.charAt(index);
            }

            @Override
            public int length() {
                return stringValue.length();
            }
        };
    }


    @Nonnull
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        addTo(builder);
        return builder.toString();
    }


    protected abstract void addTo(@Nonnull final StringBuilder stringBuilder);


    protected abstract char charAtUnchecked(@Nonnegative final int index);


    @Override
    public final char charAt(final int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        return charAtUnchecked(index);
    }


    /**
     * Returns an empty rope.
     *
     * @return a {@code Rope} of length 0.
     */
    @Nonnull
    public static Rope empty() {
        return EMPTY;
    }


    @Nonnull
    @Override
    public Rope subSequence(final int start, final int end) {
        if (start > end) {
            throw new IndexOutOfBoundsException(String.format("start:%d > end:%d", start, end));
        }
        if (end > length()) {
            throw new IndexOutOfBoundsException(String.format("end:%d > length:%d", end, length()));
        }
        if (start < 0) {
            throw new IndexOutOfBoundsException(String.format("start:%d < 0", start));
        }
        if (start == end) {
            return EMPTY;
        }
        final Rope parent = this;
        return new Rope() {
            private static final long serialVersionUID = 1L;

            @Override
            public int length() {
                return end - start;
            }

            @Override
            protected char charAtUnchecked(int index) {
                return parent.charAtUnchecked(index + start);
            }

            @Override
            protected void addTo(@Nonnull final StringBuilder stringBuilder) {
                for (int i = start; i < end; i++) {
                    stringBuilder.append(parent.charAtUnchecked(i));
                }
            }
        };
    }


    public Rope append(@Nonnull final Rope second) {
        final Rope first = this;
        return new Rope() {
            private static final long serialVersionUID = 1L;
            @Nonnegative private final int length = first.length() + second.length();

            @Override
            public int length() {
                return length;
            }

            @Override
            protected char charAtUnchecked(int index) {
                return index < first.length() ? first.charAt(index) : second.charAt(index - first.length());
            }

            @Override
            protected void addTo(@Nonnull final StringBuilder stringBuilder) {
                first.addTo(stringBuilder);
                second.addTo(stringBuilder);
            }
        };
    }


    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < length(); i++) {
            result = 31 * result + Character.hashCode(charAtUnchecked(i));
        }
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rope)) {
            return false;
        }
        final Rope that = (Rope)obj;
        if (this.length() != that.length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            if (this.charAtUnchecked(i) != that.charAtUnchecked(i)) {
                return false;
            }
        }
        return true;
    }
}
