package com.epam.esm.service.validation;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

import static com.epam.esm.service.exception.ExceptionMessage.*;

public class Validator {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";

    private static final int MAX_STRING_NAME = 50;
    private static final int MIN_STRING_NAME = 3;
    private static final int MAX_DURATION = 12;
    private static final int MIN_DURATION = 1;
    private static final double MIN_PRICE = 0.01;
    private static final double MAX_PRICE = 999999.99;

    private Validator() {
    }

    public static void validateCertificate(Certificate certificate) {
        if (certificate == null) throw new ServiceException(ERR_NO_SUCH_CERTIFICATES);
        validateName(certificate.getName());
        if (certificate.getDescription().isEmpty()) throw new ServiceException(BAD_CERTIFICATE_DESCRIPTION);
        isGreaterZero(certificate.getPrice());
        isGreaterZero(certificate.getDuration());

    }

    public static void isGreaterZero(long id) {
        if (id < 1) {
            throw new ServiceException(BAD_ID);
        }
    }

    public static void isGreaterZero(double id) {
        if (id < 1) {
            throw new ServiceException(BAD_ID);
        }
    }


    public static void validateName(String name) {
        if (name == null || name.length() < MIN_STRING_NAME || name.length() > MAX_STRING_NAME) {
            throw new ServiceException(BAD_NAME);
        }
    }

    public static void validatePrice(double price) {
        if (price < MIN_PRICE || price > MAX_PRICE) {
            throw new ServiceException(BAD_CERTIFICATE_PRICE);
        }
    }

    public static void validateDuration(int duration) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new ServiceException(BAD_CERTIFICATE_DURATION);
        }
    }

    public static void validateListOfTags(List<Tag> tags) {
        if (tags == null) throw new ServiceException(BAD_NAME);
        for (Tag tag : tags) {
            validateName(tag.getName());
        }
    }

    public static void validateOrder(Order order) {
        isGreaterZero(order.getCertificateId());
        validatePrice(order.getCost());
        isGreaterZero(order.getUserId());
    }

    public static void isUpdatesValid(Map<String, Object> updates) {
        if (updates.containsKey(NAME)) {
            validateName(updates.get(NAME).toString());
        }
        if (updates.containsKey(DESCRIPTION)) {
            Validator.validateName(updates.get(DESCRIPTION).toString());
        }
        if (updates.containsKey(PRICE)) {
            if (updates.get(PRICE).toString().matches("-?\\d+(\\.\\d+)?")) {
                Validator.validatePrice(Double.parseDouble(updates.get(PRICE).toString()));
            } else {
                throw new ServiceException(PRICE_NOT_NUMBER);
            }
        }
        if (updates.containsKey(DURATION)) {
            if (updates.get(DURATION).toString().matches("-?\\d+(\\.\\d+)?")) {
                Validator.validateDuration(Integer.parseInt(updates.get(DURATION).toString()));
            } else {
                throw new ServiceException(DURATION_NOT_NUMBER);
            }
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < MIN_STRING_NAME || password.length() > MAX_STRING_NAME) {
            throw new ServiceException(BAD_PASSWORD);
        }
    }

    public static void validateTagsDto(List<TagDto> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) throw new ServiceException(TAG_EMPTY);
    }
}
