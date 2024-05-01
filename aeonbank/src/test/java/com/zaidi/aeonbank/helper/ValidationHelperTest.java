package com.zaidi.aeonbank.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidationHelperTest {

    @Test
    void isEmail() {
        Assertions.assertTrue(ValidationHelper.isEmail("asdsad@dsadas.sql"));
        Assertions.assertFalse(ValidationHelper.isEmail("qweqw sadasd qeqwe"));
        Assertions.assertFalse(ValidationHelper.isEmail("asdasd12321"));
    }

}
