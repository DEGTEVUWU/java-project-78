package hexlet.code.schemas;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class StringSchema extends BaseSchema<String> {

    public StringSchema required() {
        addCheck(
                "required",
                value -> value instanceof String && !((String) value).isEmpty()
        );
        return this;
    }

    public StringSchema contains(String chars) {
        addCheck(
                "contains",
                value -> value == null || ((String) value).contains(chars)
        );
        return this;
    }
    public StringSchema minLength(int minLength) {
        addCheck(
                "minLength",
                value -> {
                    if (Objects.isNull(value)) {
                        return false;
                    }
                    return ((String) value).length() >= minLength;
                }
        );
        return this;
    }

}
