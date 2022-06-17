package com.epam.esm.service.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
    }

    //tags
    public static final String TAG_EXIST = "tag.alreadyExist";
    public static final String TAG_NOT_FOUND = "tag.notFound";
    public static final String TAG_EMPTY = "tag.empty";

    //certificates
    public static final String BAD_NAME = "bad.name";
    public static final String BAD_CERTIFICATE_PRICE = "certificate.badPrice";
    public static final String ERR_NO_SUCH_CERTIFICATES = "no.certificates.with.parameters";
    public static final String BAD_CERTIFICATE_DURATION = "bad.certificates.duration";
    public static final String BAD_CERTIFICATE_DESCRIPTION = "bad.certificates.description";

    //users
    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String USER_EXIST = "user.exist";
    public static final String BAD_PASSWORD = "bad.password";
    public static final String PRICE_NOT_NUMBER = "not.number";
    public static final String DURATION_NOT_NUMBER = "duration.not.number";

    public static final String INVALID_CREDENTIALS ="message.invalid.login.password";

    //orders
    public static final String ORDER_NOT_FOUND = "order.not.found";

    //general
    public static final String EMPTY_LIST = "empty.list";
    public static final String EXTRACTING_OBJECT_ERROR = "extracting.object.error";
    public static final String BAD_ID = "identifiable.badID";


}
