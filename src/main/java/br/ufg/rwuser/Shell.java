package br.ufg.rwuser;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Shell app to add users and profiles for reqweb - requisition system.
 *
 * @author andre
 */
public class Shell {

    private static Connection con;

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection(
                Configuration.getInstance().getValue("jdbcString"),
                Configuration.getInstance().getValue("dbUsername"),
                Configuration.getInstance().getValue("dbPassword"));
        con.setAutoCommit(true);
        
        ShellFactory.createConsoleShell("reqweb", "", new Shell()).commandLoop();
    }

    @Command(description = "add user into reqweb system")
    public String adduser(@Param(name = "username", description = "LDAP username") String username)
            throws SQLException {
        Map<String, LdapInfo> userMap = new HashMap();
        for (LdapInfo user : LdapInfo.scanLdap(username)) {
            userMap.put(user.getUsuario(), user);
        }
        if (userMap.size() > 0) {
            LdapInfo newUser = userMap.get(username);
            PreparedStatement userQuery = con.prepareStatement("insert into usuario (login, matricula, nome, email) values (?, ?, ?, ?)");
            userQuery.setString(1, newUser.getUsuario());
            userQuery.setString(2, newUser.getMatricula());
            userQuery.setString(3, newUser.getNome());
            userQuery.setString(4, newUser.getEmail());
            userQuery.execute();
            
            PreparedStatement profileQuery = con.prepareStatement("insert into perfil (tipoperfil, usuario_id) values (?, (select id from usuario where login = ?))");
            profileQuery.setString(1, PerfilEnum.ADMINISTRADOR.name());            
            profileQuery.setString(2, newUser.getUsuario());            
            profileQuery.execute();
            
            return String.format("the user <%s> was added.\n", username);

        } else {
            return String.format("the user <%s> is not registered in LDAP server!\n", username);
        }
    }

    @Command(description = "scans LDAP tree and prints users to the console")
    public void list() {
        List<LdapInfo> users = LdapInfo.scanLdap("");
        int listSize = users.size();
        int pageSize = 20;
        int itemCounter = 0;
        for (int i = 1; i <= users.size(); i++) {
            String input = "";
            itemCounter++;
            if (itemCounter == pageSize) {
                System.out.print("press enter to continue or q to quit: ");
                itemCounter = 0;
                Scanner in = new Scanner(System.in);
                if (in.nextLine().toLowerCase().equals("q")) {
                    return;
                }
            }
            System.out.format("%1$04d %2$-32s - %3$s\n", i, users.get(i).getUsuario(), users.get(i).getNome());
        }
    }

    @Command
    public String help() {
        StringBuilder sb = new StringBuilder();
        sb.append("commands:\n");
        sb.append("* adduser <username> -creates new user\n");
        sb.append("* list - shows all registered users in LDAP server\n");
        sb.append("* quit - closes the console\n");
        return sb.toString();
    }

    @Command
    public void quit() {
        try {
            con.close();
        } catch (Exception e) {
            System.out.println("no connections to close.");
        }
        System.out.println("good bye!");
        System.exit(0);
    }
}
