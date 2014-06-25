package br.ufg.rwuser;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.ShellFactory;
import java.io.IOException;

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
        for (int i=0;i<10;i++) {
            System.out.format("%0004d - xxxx", i);
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
