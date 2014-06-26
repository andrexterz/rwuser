package br.ufg.rwuser;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import java.io.IOException;
import java.util.List;

/**
 * Shell app to add users and profiles for reqweb - requisition system.
 * @author andre
 */
public class Shell {
    public static void main( String[] args ) throws IOException {
        ShellFactory.createConsoleShell("reqweb", "", new Shell()).commandLoop();
    }
    
    @Command(description = "add user into reqweb system")
    public String adduser(
            @Param(name="username", description = "LDAP username") String username) {
        return String.format("user <%s> was added.\n", username);
    }
    @Command(description = "scans LDAP tree and prints users to the console")
    public void list() {
        List<LdapInfo> users = LdapInfo.scanLdap();
        for (int i=0;i < users.size(); i++) {
            System.out.format("%d - %s\n", i, users.get(i));
        }
    }

    @Command
    public String help() {
        return "commands:\n\tadduser <username>\t-\tcreates new user\n\tquit";
    }
    
    @Command
    public void quit() {
        System.out.println("good bye!");
        System.exit(0);
    }
}
