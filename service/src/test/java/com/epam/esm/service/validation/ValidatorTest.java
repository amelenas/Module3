package com.epam.esm.service.validation;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";

    @ParameterizedTest
    @MethodSource("checkName_CorrectValuesTest")
    void checkPassword(String value) {
        Validator.validateName(value);
    }

    private static List<String> checkName_CorrectValuesTest() {
        List<String> values = new ArrayList<>();
        values.add("Name");
        values.add("Laura");
        values.add("Semen");
        values.add("Cream");
        values.add("Soda");
        return values;
    }

    @ParameterizedTest
    @MethodSource("checkName_FalseValuesTest")
    void checkPasswordFalse(String value) {
        assertThrows(ServiceException.class, () -> Validator.validateName(value));
    }

    private static List<String> checkName_FalseValuesTest() {
        List<String> values = new ArrayList<>();
        values.add("ff");
        values.add("");
        values.add(null);
        values.add("dfhryTnnmddgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg9");
        return values;
    }

    @Test
    void isValuePresentTest() {
        Certificate certificatePositive = new Certificate(2, "Pet store",
                "Pet store", 200.0, 12, null, null);
        Certificate certificateNegative = new Certificate(0, "",
                null, 0.0, 0, null, null);
        Certificate certificateNegative2 = new Certificate(1, null,
                "null", 0.0, 12, null, null);
        Validator.validateCertificate(certificatePositive);
        assertThrows(ServiceException.class, () -> Validator.validateCertificate(certificateNegative));
        assertThrows(ServiceException.class, () -> Validator.validateCertificate(certificateNegative2));

    }

    @Test
    void isGreaterZeroTest() {
        Validator.isGreaterZero(1);
        assertThrows(ServiceException.class, () -> Validator.isGreaterZero(0L));
        assertThrows(ServiceException.class, () -> Validator.isGreaterZero(-1L));
    }

    @Test
    void isGreaterZeroDoubleTest() {
        assertThrows(ServiceException.class, () -> Validator.isGreaterZero(0D));
        assertThrows(ServiceException.class, () -> Validator.isGreaterZero(-1D));
    }

    @Test
    void validatePriceTest() {
        assertThrows(ServiceException.class, () -> Validator.validatePrice(0D));
        assertThrows(ServiceException.class, () -> Validator.validatePrice(-10D));
    }

    @Test
    void validateDurationTest() {
        assertThrows(ServiceException.class, () -> Validator.validateDuration(0));
        assertThrows(ServiceException.class, () -> Validator.validateDuration(13));
    }

    @Test
    void validatePasswordTest() {
        assertThrows(ServiceException.class, () -> Validator.validatePassword("0"));
        assertThrows(ServiceException.class, () -> Validator.validatePassword(null));
    }

    @Test
    void validateOrderTest_Positive() {
        Order order = new Order(1, 12, 1, Instant.now());
        Validator.validateOrder(order);
    }

    @Test
    void validateOrderTest_Negative() {
        Order order = new Order(-1, 0, 1, Instant.now());
        assertThrows(ServiceException.class, () -> Validator.validateOrder(order));
    }

    @Test
    void isUpdatesValidName_Negative() {
        Map<String, Object> updates = new HashMap<>();
        updates.put(NAME, "");
        assertThrows(ServiceException.class, () -> Validator.isUpdatesValid(updates));
    }

    @Test
    void isUpdatesValidDescription_Negative() {
        Map<String, Object> updates = new HashMap<>();
        updates.put(DESCRIPTION, "");
        assertThrows(ServiceException.class, () -> Validator.isUpdatesValid(updates));
    }

    @Test
    void isUpdatesValidPriceNegative() {
        Map<String, Object> updates = new HashMap<>();
        updates.put(PRICE, -12.);
        assertThrows(ServiceException.class, () -> Validator.isUpdatesValid(updates));
    }

    @Test
    void isUpdatesValidDurationNegative() {
        Map<String, Object> updates = new HashMap<>();
        updates.put(DURATION, 0);
        assertThrows(ServiceException.class, () -> Validator.isUpdatesValid(updates));
    }

    @Test
    void validateListOfTagsTest() {
        List<Tag> tagsPositive = new ArrayList<>();
        tagsPositive.add(new Tag(11, "TestTag"));
        tagsPositive.add(new Tag(1, "TestTag2"));

        List<Tag> tagsNegative = new ArrayList<>();
        tagsNegative.add(new Tag(11, ""));
        tagsNegative.add(new Tag(1, null));

        List<Tag> tagsNegative2 = new ArrayList<>();
        tagsNegative2.add(new Tag(11, "Test tag"));
        tagsNegative2.add(new Tag(1, null));

        List<Tag> tagsNull = null;

        Validator.validateListOfTags(tagsPositive);
        assertThrows(ServiceException.class, () -> Validator.validateListOfTags(tagsNegative));
        assertThrows(ServiceException.class, () -> Validator.validateListOfTags(tagsNegative2));
        assertThrows(ServiceException.class, () -> Validator.validateListOfTags(tagsNull));
    }
}