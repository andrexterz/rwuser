package br.ufg.rwuser;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Classe responsavel por localizar um contexto de diretÃ³rio LDAP
 */
public class LdapServiceLocator {

    protected static LdapServiceLocator instance;

    private LdapServiceLocator() {
        super();
    }

    /**
     * Obtem a mesma instancia de LdapServiceLocator para todas as chamadas
 (Classe singleton)
     *
     * @return um objeto LdapServiceLocator
     */
    public static LdapServiceLocator getInstance() {

        if (instance == null) {
            instance = new LdapServiceLocator();
        }

        return instance;
    }

    public DirContext getContext(String usuario, String senha) throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, Configuration.getInstance().getValue("INITIAL_CTX"));
        env.put(Context.PROVIDER_URL, Configuration.getInstance().getValue("SERVIDOR"));
        env.put(Context.SECURITY_AUTHENTICATION, "simple");   //com none não confere senha // auf
        env.put(Context.SECURITY_PRINCIPAL, usuario);        //auf
        env.put(Context.SECURITY_CREDENTIALS, senha);      //auf

        DirContext ctx = null;

        // Obtem um Initial Context
        ctx = new InitialDirContext(env);


        return ctx;
    }
}