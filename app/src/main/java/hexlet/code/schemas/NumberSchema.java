package hexlet.code.schemas;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class NumberSchema extends BaseSchema<Integer> {
    public NumberSchema required() {
        addCheck(
                "required",
                Objects::nonNull);
        return this;
    }

    public NumberSchema positive() {
        addCheck(
                "positive",
                value -> value == null || ((Integer) value) > 0
        );
        return this;
    }

    public NumberSchema range(int min, int max) {
        addCheck(
                "range",
                value -> value != null && ((int) value) >= min && ((int) value <= max)
        );
        return this;
    }


}
