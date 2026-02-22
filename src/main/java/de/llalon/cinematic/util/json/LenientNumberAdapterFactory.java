package de.llalon.cinematic.util.json;

import com.squareup.moshi.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Moshi {@link JsonAdapter.Factory} that leniently coerces JSON values into {@link Number} types.
 *
 * <p>Supported coercions:</p>
 * <ul>
 *     <li>JSON numbers → native number types</li>
 *     <li>Quoted numbers (e.g. "123") → parsed into numeric values</li>
 *     <li>Empty strings ("") → {@code null}</li>
 *     <li>Null values → {@code null}</li>
 * </ul>
 *
 * <p>Supported number targets:</p>
 * <ul>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link Double}</li>
 *     <li>{@link Float}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Byte}</li>
 *     <li>{@link BigDecimal}</li>
 *     <li>{@link BigInteger}</li>
 * </ul>
 *
 * <p>Warning: This adapter performs lenient coercion. Malformed numeric values
 * will throw {@link JsonDataException}.</p>
 */
public class LenientNumberAdapterFactory implements JsonAdapter.Factory {

    /**
     * Creates a lenient number adapter when the requested type is a numeric type.
     *
     * @param type target deserialization type
     * @param annotations field or parameter annotations
     * @param moshi moshi instance
     * @return number adapter or {@code null} if type is not supported
     */
    @Nullable
    @Override
    public JsonAdapter<?> create(
            @NotNull Type type, @NotNull Set<? extends Annotation> annotations, @NotNull Moshi moshi) {

        Class<?> raw = Types.getRawType(type);

        if (!isSupportedNumberType(raw)) {
            return null;
        }

        JsonAdapter<Object> delegate = moshi.nextAdapter(this, type, annotations);

        return new JsonAdapter<Object>() {

            @Override
            public @Nullable Object fromJson(@NotNull JsonReader reader) throws IOException {

                JsonReader.Token token = reader.peek();

                if (token == JsonReader.Token.NULL) {
                    reader.nextNull();
                    return null;
                }

                if (token == JsonReader.Token.STRING) {
                    String value = reader.nextString();
                    if (value == null || value.trim().isEmpty()) {
                        return null;
                    }
                    return parseNumber(value, raw);
                }

                if (token == JsonReader.Token.NUMBER) {
                    return parseNumber(reader.nextString(), raw);
                }

                throw new JsonDataException("Unexpected JSON token for Number: " + token);
            }

            @Override
            public void toJson(@NotNull JsonWriter writer, @Nullable Object value) throws IOException {
                delegate.toJson(writer, value);
            }
        };
    }

    /**
     * Determines whether the provided class represents a supported number type.
     */
    private boolean isSupportedNumberType(Class<?> raw) {
        return Number.class.isAssignableFrom(raw)
                || raw == int.class
                || raw == long.class
                || raw == double.class
                || raw == float.class
                || raw == short.class
                || raw == byte.class;
    }

    /**
     * Parses a string into the appropriate numeric type.
     *
     * @param value numeric string value
     * @param raw target numeric class
     * @return parsed number or {@code null} if value is blank
     */
    @Nullable
    private Object parseNumber(@NotNull String value, @NotNull Class<?> raw) {

        String trimmed = value.trim();

        if (trimmed.isEmpty()) {
            return null;
        }

        if (raw == Integer.class || raw == int.class) {
            return Integer.valueOf(trimmed);
        }
        if (raw == Long.class || raw == long.class) {
            return Long.valueOf(trimmed);
        }
        if (raw == Double.class || raw == double.class) {
            return Double.valueOf(trimmed);
        }
        if (raw == Float.class || raw == float.class) {
            return Float.valueOf(trimmed);
        }
        if (raw == Short.class || raw == short.class) {
            return Short.valueOf(trimmed);
        }
        if (raw == Byte.class || raw == byte.class) {
            return Byte.valueOf(trimmed);
        }
        if (raw == BigDecimal.class) {
            return new BigDecimal(trimmed);
        }
        if (raw == BigInteger.class) {
            return new BigInteger(trimmed);
        }

        return null;
    }
}
