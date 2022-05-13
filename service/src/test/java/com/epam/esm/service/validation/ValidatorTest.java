package com.epam.esm.service.validation;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.exception.ExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    @ParameterizedTest
    @MethodSource("checkName_CorrectValues")
    public void checkPassword(String value) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        assertTrue(Validator.validateName(value, exceptionHandler));
    }

    private static List<String> checkName_CorrectValues() {
        List<String> values = new ArrayList<>();
        values.add("Name");
        values.add("Laura");
        values.add("Semen");
        values.add("Cream");
        values.add("Soda");
        return values;
    }

    @ParameterizedTest
    @MethodSource("checkName_FalseValues")
    public void checkPasswordFalse(String value) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        assertFalse(Validator.validateName(value, exceptionHandler));
    }

    private static List<String> checkName_FalseValues() {
        List<String> values = new ArrayList<>();
        values.add("ff");
        values.add("");
        values.add(null);
        values.add("dfhryTnnmddgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg9");
        return values;
    }

    @Test
    void isValuePresent() {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        Certificate certificatePositive = new Certificate(2, "Pet store",
                "Pet store", 200.0, 12, null, null);
        Certificate certificateNegative = new Certificate(0, "",
                null, 0.0, 0, null, null);
        Certificate certificateNegative2 = new Certificate(1, null,
                "null", 0.0, 12, null, null);
        assertTrue(Validator.validateCertificate(certificatePositive, exceptionHandler));
        assertFalse(Validator.validateCertificate(certificateNegative, exceptionHandler));
        assertFalse(Validator.validateCertificate(certificateNegative2, exceptionHandler));

    }

    @Test
    void isGreaterZero() {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        assertTrue(Validator.isGreaterZero(1, exceptionHandler));
        assertFalse(Validator.isGreaterZero(0, exceptionHandler));
        assertFalse(Validator.isGreaterZero(-1, exceptionHandler));
    }


    @Test
    void validateListOfTags() {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        List<Tag> tagsPositive=new ArrayList<>();
        tagsPositive.add(new Tag(11, "TestTag"));
        tagsPositive.add(new Tag(1, "TestTag2"));

        List<Tag> tagsNegative=new ArrayList<>();
        tagsNegative.add(new Tag(11, ""));
        tagsNegative.add(new Tag(1, null));

        List<Tag> tagsNegative2=new ArrayList<>();
        tagsNegative2.add(new Tag(11, "Test tag"));
        tagsNegative2.add(new Tag(1, null));

        List<Tag> tagsNull=null;

        assertTrue(Validator.validateListOfTags(tagsPositive, exceptionHandler));
        assertFalse(Validator.validateListOfTags(tagsNegative, exceptionHandler));
        assertFalse(Validator.validateListOfTags(tagsNegative2, exceptionHandler));
        assertFalse(Validator.validateListOfTags(tagsNull, exceptionHandler));
    }
}