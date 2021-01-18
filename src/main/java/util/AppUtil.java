package util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author : Lucky Prabath <lucky.prabath94@gmail.com>
 * @since : 2021-01-11
 **/
public class AppUtil {

    public static String getAppSecretKey() throws IOException {
        Properties prop = new Properties();
        prop.load(AppUtil.class.getResourceAsStream("/application.properties"));
        return prop.getProperty("app.key");
    }
}

