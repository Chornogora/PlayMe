package com.dataart.playme.util;

import java.util.Properties;

public final class Constants {

    public static final String DELETED_USER_MARK = "_deleted_on_";

    public static final String NOW = "now";

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public static final String OPERATION_STATUS_CONTEXT_PARAMETER = "operationStatus";

    public static final String APPLICATION_PATH = "/";

    public static final String FRONTEND_AUTHORIZATION_PATH = "http://localhost:5000/login.html";

    public static final String FILE_STORAGE_PATH_ID = "storage.path.root";

    public static final String IMAGE_ROOT_DIRECTORY_ID = "storage.path.images";

    public static final String FILE_ROOT_DIRECTORY_ID = "storage.path.files";

    private static Properties properties;

    public static String get(String constantName) {
        return properties.getProperty(constantName);
    }

    public static void setProperties(Properties properties) {
        Constants.properties = properties;
    }

    public static class Constraints {

        public static final int MAX_LOGIN_LENGTH = 64;

        public static final int MIN_LOGIN_LENGTH = 8;

        public static final String LOGIN_PATTERN = "[a-zA-Z0-9.-_@]*";

        public static final int MAX_PASSWORD_LENGTH = 64;

        public static final int MIN_PASSWORD_LENGTH = 8;

        public static final int MAX_EMAIL_LENGTH = 320;

        public static final String EMAIL_PATTERN = "^[A-Za-z0-9+%!-]+(\\.?[a-zA-Z0-9+%!-]*)*@[a-z0-9-]+(\\.[a-z0-9]+)*";

        public static final int MAX_NAME_LENGTH = 64;

        public static final int MIN_NAME_LENGTH = 2;

        public static final String NAME_PATTERN = "[a-zA-Z-]*";

        public static final String MAX_BIRTHDATE = "2014-11-01_00-00-00";

        public static final String MIN_BIRTHDATE = "1900-01-01_00-00-00";

        public static final String MIN_CREATION_DATE = "2020-11-13_00-00-00";
    }

    public static class Security {

        public static final String SESSION_LIFETIME = "security.session.lifetime";

        public static final String HS256_SECRET_KEY = "security.hs256.secret";

        public static final String JWT_TOKEN_COOKIE_NAME = "token";

        public static final String ENCODING_ID = "security.encoding";
    }
}
