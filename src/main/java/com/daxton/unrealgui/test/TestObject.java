package com.daxton.unrealgui.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.function.Function;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TestObject {

    private String x = "5X";
    private String y = "6Y";

    public void applyFunctionToFields(Function<String, String> function) {
        Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(String.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(this);
                        String result = function.apply(value);
                        field.set(this, result);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void applyFunctionToFields2(Function<String, String> function) {
        setX(function.apply(getX()));
        setY(function.apply(getY()));
    }
}
