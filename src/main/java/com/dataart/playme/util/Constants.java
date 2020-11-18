package com.dataart.playme.util;

import java.util.Properties;

public final class Constants {

    public static final String ACTIVE_STATUS_ID = "user.status.active";

    public static final String DISABLED_STATUS_ID = "user.status.disabled";

    public static final String DELETED_STATUS_ID = "user.status.deleted";

    public static final String DELETED_USER_MARK = "_deleted_on_";

    private static Properties properties;

    public static String get(String constantName) {
        return properties.getProperty(constantName);
    }

    public static void setProperties(Properties properties) {
        Constants.properties = properties;
    }
}
