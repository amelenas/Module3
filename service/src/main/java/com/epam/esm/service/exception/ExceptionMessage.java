package com.epam.esm.service.exception;

public class ExceptionMessage {

    //tags
    public static final String TAG_EXIST = "tag.alreadyExist";
    public static final String TAG_NOT_FOUND = "tag.notFound";

    //certificates
    public static final String BAD_NAME = "bad.name";
    public static final String BAD_CERTIFICATE_PRICE = "certificate.badPrice";
    public static final String ERR_NO_SUCH_CERTIFICATES = "no.certificates.with.parameters";
    public static final String BAD_CERTIFICATE_DURATION = "bad.certificates.duration";

    //users
    public static final String USER_NOT_FOUND = "user.not.found";

    //orders
    public static final String ORDER_NOT_FOUND = "order.not.found";

    //general
    public static final String EMPTY_LIST = "empty.list";
    public static final String EXTRACTING_OBJECT_ERROR = "extracting.object.error";
    public static final String PROBLEM_CREATE = "problem.create";
    public static final String PROBLEM_UPDATE = "problem.update";
    public static final String BAD_ID = "identifiable.badID";
}
