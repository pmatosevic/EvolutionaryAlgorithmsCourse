package hr.fer.zemris.optjava.rng;

import java.util.Properties;

public class RNG {

    private static IRNGProvider rngProvider;

    static {
        try {
            ClassLoader cl = RNG.class.getClassLoader();
            Properties properties = new Properties();
            properties.load(cl.getResourceAsStream("rng-config.properties"));
            Class<?> providerClass = cl.loadClass(properties.getProperty("rng-provider"));
            rngProvider = (IRNGProvider) providerClass.getDeclaredConstructors()[0].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }


}
