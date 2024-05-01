package com.zaidi.aeonbank.helper;

import java.util.regex.Pattern;

public class ValidationHelper {

    public ValidationHelper() {
        throw new RuntimeException("Helper class");
    }

    public static boolean isEmail(String email) {
        return Pattern
                .compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                .matcher(email)
                .matches();
    }

}
