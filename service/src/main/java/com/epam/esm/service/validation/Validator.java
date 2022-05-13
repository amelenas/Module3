package com.epam.esm.service.validation;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.exception.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.esm.service.exception.ExceptionMessage.*;

public class Validator {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";

    private static final int MAX_LENGTH_NAME = 50;
    private static final int MIN_LENGTH_NAME = 3;
    private static final int MAX_DURATION = 12;
    private static final int MIN_DURATION = 1;
    private static final double MIN_PRICE = 0.01;
    private static final double MAX_PRICE = 999999.99;

    private Validator() {
    }

    public static boolean validateCertificate(Certificate certificate, ExceptionHandler exceptionHandler) {
        return certificate != null &&
                validateName(certificate.getName(), exceptionHandler) &&
                certificate.getDescription() != null &&
                isGreaterZero(certificate.getPrice(), exceptionHandler) &&
                isGreaterZero(certificate.getDuration(), exceptionHandler);
    }

    public static boolean isGreaterZero(long id, ExceptionHandler exceptionHandler) {
        if (id < 1) {
            exceptionHandler.addException(BAD_ID, id);
            return false;
        }
        return true;
    }

    public static boolean isGreaterZero(double id, ExceptionHandler exceptionHandler) {
        if (id < 1) {
            exceptionHandler.addException(BAD_ID, id);
            return false;
        }
        return true;
    }


    public static boolean validateName(String name, ExceptionHandler exceptionResult) {
        if (name == null || name.length() < MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            exceptionResult.addException(BAD_NAME, name);
            return false;
        }
        return true;
    }

    public static boolean validatePrice(double price, ExceptionHandler exceptionResult) {
        if (price < MIN_PRICE || price > MAX_PRICE) {
            exceptionResult.addException(BAD_CERTIFICATE_PRICE, price);
            return true;
        }
        return false;
    }

    public static boolean validateDuration(int duration, ExceptionHandler exceptionResult) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            exceptionResult.addException(BAD_CERTIFICATE_DURATION, duration);
            return false;
        }
        return true;
    }

    public static boolean validateListOfTags(List<Tag> tags, ExceptionHandler exceptionResult) {
        if (tags == null) return false;
        List<Boolean> validatedTags = new ArrayList<>();
        for (Tag tag : tags) {
            validatedTags.add(validateName(tag.getName(), exceptionResult));
        }
        return !validatedTags.contains(false);
    }

    public static boolean validateOrder(Order order, ExceptionHandler exceptionHandler) {
        return !isGreaterZero(order.getOrderId(), exceptionHandler) &&
                !isGreaterZero(order.getCertificateId(), exceptionHandler) &&
                validatePrice(order.getCost(), exceptionHandler) &&
                !isGreaterZero(order.getUserId(), exceptionHandler);
    }

    public static boolean isUpdatesValid(Map<String, Object> updates, ExceptionHandler exceptionHandler) {
        if (updates.containsKey(NAME) && !validateName(updates.get(NAME).toString(), exceptionHandler) ||
                updates.containsKey(DESCRIPTION) && !Validator.validateName(updates.get(DESCRIPTION).toString(), exceptionHandler)){
                exceptionHandler.addException(BAD_NAME, updates);
                return false;
            }

        if (updates.containsKey(PRICE) && Validator.validatePrice((Double) updates.get(PRICE), exceptionHandler)){
                exceptionHandler.addException(BAD_CERTIFICATE_PRICE, updates);
                return false;

        }
        if (updates.containsKey(DURATION) && !Validator.validateDuration((Integer) updates.get(DURATION), exceptionHandler)){
                exceptionHandler.addException(BAD_CERTIFICATE_DURATION, updates);
                return false;
        }
        return true;
    }
}
