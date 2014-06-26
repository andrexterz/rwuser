/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.rwuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

/**
 *
 * @author andre
 */
public class LdapInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String usuario;
    private String nome;
    private String grupo;
    private String uid;
    private String matricula;
    private String email;
    static private DirContext ctx;


    public  static List<LdapInfo> scanLdap(String username) {
        List<LdapInfo> usuarios = new ArrayList<>();
        Attributes matchAttrs = new BasicAttributes(false);
        if (!username.isEmpty()) {
            matchAttrs.put(new BasicAttribute("uid", username));
        }
        String[] atributosRetorno = new String[]{"mail", "cn", "uid", "uidNumber", "gidNumber"};
        NamingEnumeration<?> resultado;
        try {
            ctx = LdapServiceLocator.getInstance().getContext(username);
            resultado = ctx.search(
                    Configuration.getInstance().getValue("searchBase"),
                    matchAttrs,
                    atributosRetorno);

            while (resultado.hasMore()) {
                LdapInfo itemInfo = new LdapInfo();
                SearchResult sr = (SearchResult) resultado.next();
                Attributes atributos = sr.getAttributes();

                for (NamingEnumeration<?> todosAtributos = atributos.getAll(); todosAtributos.hasMore();) {
                    Attribute attrib = (Attribute) todosAtributos.next();
                    String nomeAtributo = attrib.getID();

                    if (nomeAtributo.equals("gidNumber")) {
                        itemInfo.setGrupo((String) attrib.get());
                    }
                    if (nomeAtributo.equals("uid")) {
                        itemInfo.setUsuario((String) attrib.get());
                        if (itemInfo.grupo.equals("500")) {
                            Pattern patt = Pattern.compile("\\d+");
                            Matcher mat = patt.matcher(itemInfo.getUsuario());
                            if (mat.find()) {
                                itemInfo.setMatricula(mat.group());
                            }
                        } else {
                            itemInfo.setMatricula(null);
                        }                        
                    }
                    if (nomeAtributo.equals("mail")) {
                        itemInfo.setEmail((String) attrib.get());
                    }
                    if (nomeAtributo.equals("cn")) {
                        itemInfo.setNome((String) attrib.get());
                    }
                    if (nomeAtributo.equals("uidNumber")) {
                        itemInfo.setUid((String) attrib.get());
                    }

                }
                if (!itemInfo.getGrupo().equals(PerfilEnum.ADMINISTRADOR.getGrupo())) {
                    usuarios.add(itemInfo);
                }

            }
        } catch (NamingException ex) {
            Logger.getLogger(LdapInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    private void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the grupo
     */
    public String getGrupo() {
        return grupo;
    }

    private void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    /**
     * 
     * @return 
     */
    public String getUid() {
        return uid;
    }
    
    /**
     * 
     * @param uid 
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    private void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

}
