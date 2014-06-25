package br.ufg.rwuser;

import asg.cliche.Command;
import asg.cliche.ShellFactory;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class Shell {
    public static void main( String[] args ) throws IOException {
        ShellFactory.createConsoleShell("reqweb", "", new Shell()).commandLoop();
    }
    
    @Command
    public String help() {
        return "help text here";
    }
}
