package com.dataart.playme.util;

import java.util.Properties;

public final class Constants {

    public static final String ACTIVE_STATUS_ID = "user.status.active";

    public static final String DISABLED_STATUS_ID = "user.status.disabled";

    public static final String DELETED_STATUS_ID = "user.status.deleted";

    public static final String DELETED_USER_MARK = "_deleted_on_";

    public static final String ENCODING_ID = "security.encoding";

    public static final String USER_ROLE_ID = "user.role.user";

    public static final String ADMIN_ROLE_ID = "user.role.admin";

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

    public static void setProperties(Properties properties) {
        Constants.properties = properties;
    }
}
