package br.ufg.rwuser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Parametros de configuração do LDAP
 *
 */
public class Configuration {
    
    private final Properties config;
    
    private Configuration() {
        config = new Properties();
        try {
            InputStream in = new FileInputStream("rwuser.properties");
            config.load(in);
        } catch (IOException e) {
            System.out.format("can't get instance because error: %s", e.toString());
        }
    }

    protected static Configuration instance;

    public static final String initialContext = "com.sun.jndi.ldap.LdapCtxFactory";
    public static final String ldapServer = "ldap://200.137.197.214:389";
    public static final String searchBase = "ou=Users, dc=dionisio, dc=inf, dc=ufg, dc=br";
    public static final String baseDN = "dc=dionisio, dc=inf, dc=ufg, dc=br";

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    public String getValue(String value) {
        return config.getProperty(value);
    }
}
