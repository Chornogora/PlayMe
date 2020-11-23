package com.dataart.playme.util;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public final class Constants {

    public static final String ACTIVE_STATUS_ID = "user.status.active";

    public static final String DISABLED_STATUS_ID = "user.status.disabled";

    public static final String DELETED_STATUS_ID = "user.status.deleted";

    public static final String DELETED_USER_MARK = "_deleted_on_";

    public static final String ENCODING_ID = "security.encoding";

    public static final String ROLE_GROUP_ID = "user.role";

    public static final String STATUS_GROUP_ID = "user.status";

    public static final String USER_ROLE_ID = "user.role.user";

    public static final String ADMIN_ROLE_ID = "user.role.admin";

    public static final String DATE_PATTERN_ID = "date.pattern";

    public static final String ROLE_ATTRIBUTE = "role";

    public static final String SESSION_LIFETIME = "session.lifetime";

    public static final String SESSION_LAST_ACTIVATION_TIME_ATTRIBUTE = "last_active";

    public static final String APPLICATION_PATH = "/PlayMe";

    public static final String USER_MAIN_PAGE = "/PlayMe/main.html";

    public static final String ADMIN_MAIN_PAGE = "/PlayMe/main-admin.html";

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

    public enum CONSTRAINTS {
        MAX_LOGIN_LENGTH("constraints.max-login-length"),
        MIN_LOGIN_LENGTH("constraints.min-login-length"),
        LOGIN_PATTERN("constraints.login.regexp"),
        MAX_PASSWORD_LENGTH("constraints.max-password-length"),
        MIN_PASSWORD_LENGTH("constraints.min-password-length"),
        EMAIL_PATTERN("constraints.email.regexp"),
        MAX_NAME_LENGTH("constraints.max-name-length"),
        MIN_NAME_LENGTH("constraints.min-name-length"),
        NAME_PATTERN("constraints.name.regexp"),
        MIN_BIRTHDATE("constraints.birthdate.min");

        private final String value;

        CONSTRAINTS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
