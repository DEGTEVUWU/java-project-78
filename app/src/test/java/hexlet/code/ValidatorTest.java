package hexlet.code;

import hexlet.code.schemas.BaseSchema;
import hexlet.code.schemas.MapSchema;
import hexlet.code.schemas.NumberSchema;
import hexlet.code.schemas.StringSchema;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    @Test
    void stringMethodTest() {
        Validator v = new Validator();
        StringSchema stringObj = v.string();

        var actual1 = stringObj.isValid(null);
        var actual2 = stringObj.isValid("");
        var actual3 = stringObj.required().isValid(null);
        var actual4 = stringObj.required().isValid("");
        var actual5 = stringObj.required().isValid("Some");
        var actual6 = stringObj.required().contains("w").isValid("Some");
        var actual7 = stringObj.required().contains("So").isValid("Some");

        assertThat(actual1).isTrue();
        assertThat(actual2).isTrue();
        assertThat(actual3).isFalse();
        assertThat(actual4).isFalse();
        assertThat(actual5).isTrue();
        assertThat(actual6).isFalse();
        assertThat(actual7).isTrue();

    }

    @Test
    void numberMethodTest() {
        Validator v = new Validator();
        NumberSchema numberObj = v.number();

        var actual1 = numberObj.isValid(null);
        var actual2 = numberObj.required().isValid(null);
        var actual3 = numberObj.required().isValid(2);
        var actual4 = numberObj.required().isValid(-2);
        var actual5 = numberObj.required().positive().isValid(-2);
        var actual6 = numberObj.required().positive().isValid(2);
        var actual7 = numberObj.required().positive().range(2, 6).isValid(4);
        var actual8 = numberObj.required().positive().range(2, 6).isValid(2);
        var actual9 = numberObj.required().positive().range(2, 6).isValid(6);
        var actual10 = numberObj.required().positive().range(2, 6).isValid(7);

        assertThat(actual1).isTrue();
        assertThat(actual2).isFalse();
        assertThat(actual3).isTrue();
        assertThat(actual4).isTrue();
        assertThat(actual5).isFalse();
        assertThat(actual6).isTrue();
        assertThat(actual7).isTrue();
        assertThat(actual8).isTrue();
        assertThat(actual9).isTrue();
        assertThat(actual10).isFalse();

    }

    @Test
    void mapMethodTest() {
        Validator v = new Validator();
        MapSchema mapObj = v.map();

        Map emptyMap = new HashMap();
        Map notEmptyMap = new HashMap();
        notEmptyMap.put("key", "value");

        var actual1 = mapObj.isValid(null);
        var actual2 = mapObj.required().isValid(null);
        var actual3 = mapObj.required().isValid(emptyMap);
        var actual4 = mapObj.required().isValid(notEmptyMap);
        var actual5 = mapObj.required().sizeof(1).isValid(emptyMap);
        var actual6 = mapObj.required().sizeof(1).isValid(notEmptyMap);
        var actual7 = mapObj.required().sizeof(2).isValid(notEmptyMap);


        assertThat(actual1).isTrue();
        assertThat(actual2).isFalse();
        assertThat(actual3).isTrue();
        assertThat(actual4).isTrue();
        assertThat(actual5).isFalse();
        assertThat(actual6).isTrue();
        assertThat(actual7).isFalse();
    }

    @Test
    void mapMethodTestWithNestedValidationTesting() {
        Validator v = new Validator();
        MapSchema mapObj = v.map();
        Map<String, BaseSchema<String>> schemas = new HashMap<>();

        schemas.put("key", v.string().required());
        schemas.put("key2", v.string().required().minLength(2));

        mapObj.shape(schemas);

        Map<String, String> nestedValidation1 = new HashMap();
        nestedValidation1.put("key", "value");
        nestedValidation1.put("key2", "value2");

        Map<String, String> nestedValidation2 = new HashMap();
        nestedValidation2.put("key", "");
        nestedValidation2.put("key2", "value2");

        Map<String, String> nestedValidation3 = new HashMap();
        nestedValidation3.put("key", "value");
        nestedValidation3.put("key2", "V");

        Map<String, String> nestedValidation4 = new HashMap();
        nestedValidation4.put("key", null);
        nestedValidation4.put("key2", "value2");

        Map<String, String> nestedValidation5 = new HashMap();
        nestedValidation5.put("key", "value");
        nestedValidation5.put("key2", "VA");

        var actual1 = mapObj.isValid(nestedValidation1);
        var actual2 = mapObj.isValid(nestedValidation2);
        var actual3 = mapObj.isValid(nestedValidation3);
        var actual4 = mapObj.isValid(nestedValidation4);
        var actual5 = mapObj.isValid(nestedValidation5);

        assertThat(actual1).isTrue();
        assertThat(actual2).isFalse();
        assertThat(actual3).isFalse();
        assertThat(actual4).isFalse();
        assertThat(actual5).isTrue();

    }
    /*
    @Test
    void numberMethodTest() {
        var actual1 = new Validator().number();
        actual1.setAllowNull(true);
        actual1.setAllowPositive(true);
        actual1.setMinRange(notAMagicNumberForTests3);
        actual1.setMaxRange(notAMagicNumberForTest6);


        var expected1 = true;
        var expected2 = true;
        var expected3 = notAMagicNumberForTests3;
        var expected4 = notAMagicNumberForTest6;

        assertThat(actual1.getAllowNull()).isEqualTo(expected1);
        assertThat(actual1.getAllowPositive()).isEqualTo(expected2);
        assertThat(actual1.getMinRange()).isEqualTo(expected3);
        assertThat(actual1.getMaxRange()).isEqualTo(expected4);
    }

    @Test
    void mapMethodTest() {
        var baseSchemaTestObj = new Validator();
        var actual1 = new Validator().map();
        actual1.setAllowNull(true);
        actual1.setAllowShape(true);
        actual1.setQuantityPair(notAMagicNumberForTests3);

        var mapWithSchema = new HashMap<String, BaseSchema<String>>();
        mapWithSchema.put("key1", baseSchemaTestObj.string().required());
        mapWithSchema.put("key2", baseSchemaTestObj.string().required().contains("val"));

        var expected1 = true;
        var expected2 = true;
        var expected3 = notAMagicNumberForTests3;

        assertThat(actual1.isAllowNull()).isEqualTo(expected1);
        assertThat(actual1.isAllowShape()).isEqualTo(expected2);
        assertThat(actual1.getQuantityPair()).isEqualTo(expected3);
    }
    @Test
    void mapMethodTestWithSchemas() {
        var v = new Validator();
        var schema = v.map();

        Map<String, BaseSchema<String>> schemas = new HashMap<>();
        schemas.put("firstName", v.string().required());
        schemas.put("lastName", v.string().required().minLength(notAMagicNumberForTests2));

        schema.shape(schemas);

        Map<String, String> human1 = new HashMap<>();
        human1.put("firstName", "John");
        human1.put("lastName", "Smith");

        Map<String, String> human2 = new HashMap<>();
        human2.put("firstName", "John");
        human2.put("lastName", null);

        Map<String, String> human3 = new HashMap<>();
        human3.put("firstName", "Anna");
        human3.put("lastName", "B");

        var actual1 = schema.isValid(human1);
        var actual2 = schema.isValid(human2);
        var actual3 = schema.isValid(human3);

        var expected1 = true;
        var expected2 = false;
        var expected3 = false;

        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);


    }

     */
}
