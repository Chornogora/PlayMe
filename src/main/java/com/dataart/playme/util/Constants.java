package com.dataart.playme.util;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public final class Constants {

    public static final String ACTIVE_STATUS_ID = "user.status.active";

    public static final String DISABLED_STATUS_ID = "user.status.disabled";

    public static final String DELETED_STATUS_ID = "user.status.deleted";

    public static final String PENDING_STATUS_ID = "user.status.pending";

    public static final String DELETED_USER_MARK = "_deleted_on_";

    public static final String WEB_DATE_FORMAT = "yyyy-MM-dd";

    public static final String ENCODING_ID = "security.encoding";

    public static final String ROLE_GROUP_ID = "user.role";

    public static final String STATUS_GROUP_ID = "user.status";

    public static final String USER_ROLE_ID = "user.role.user";

    public static final String ADMIN_ROLE_ID = "user.role.admin";

    public static final String ROLE_ATTRIBUTE = "role";

    public static final String NOW = "now";

    public static final String REDIRECT_PREFIX = "redirect:";

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    public static final String SESSION_LIFETIME = "session.lifetime";

    public static final String SESSION_LAST_ACTIVATION_TIME_ATTRIBUTE = "last_active";

    public static final String OPERATION_STATUS_CONTEXT_PARAMETER = "operationStatus";

    public static final String APPLICATION_PATH = "/";

    public static final String LOGIN_PAGE_PATH = "/auth";

    public static final String USER_MAIN_PAGE = "/main.html";

    public static final String ADMIN_MAIN_PAGE = "/admin/users";

    private static Properties properties;

    public static String get(String constantName) {
        return properties.getProperty(constantName);
    }

    public static List<String> getByGroup(String groupName) {
        return properties.entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith(groupName))
                .map(entry -> entry.getValue().toString())
                .collect(Collectors.toList());
    }

    public static void setProperties(Properties properties) {
        Constants.properties = properties;
    }

    public static class Constraints {

        public static final int MAX_LOGIN_LENGTH = 64;

        public static final int MIN_LOGIN_LENGTH = 8;

        public static final String LOGIN_PATTERN = "[a-zA-Z0-9.-_@]*";

        public static final int MAX_PASSWORD_LENGTH = 64;

        public static final int  MIN_PASSWORD_LENGTH = 8;

        public static final int MAX_EMAIL_LENGTH = 320;

        public static final String  EMAIL_PATTERN = "^[A-Za-z0-9+%!-]+(\\.?[a-zA-Z0-9+%!-]*)*@[a-z0-9-]+(\\.[a-z0-9]+)*";

        public static final int MAX_NAME_LENGTH = 64;

        public static final int MIN_NAME_LENGTH = 2;

        public static final String NAME_PATTERN = "[a-zA-Z-]*";

        public static final String MAX_BIRTHDATE = "2014-11-01_00-00-00";

        public static final String  MIN_BIRTHDATE = "1900-01-01_00-00-00";

        public static final String MIN_CREATION_DATE = "2020-11-13_00-00-00";
    }
}
