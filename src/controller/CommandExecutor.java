/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mo22
 */
public class CommandExecutor {
    private Process process;
    private boolean itsWindows;
    
    public CommandExecutor(boolean itsWindows){
        this.itsWindows = itsWindows;
    }
    
    private String[] systemCommand(String command){
        if(!itsWindows){
            return new String[]{"sh","-c",command};
        } else {
            return new String[]{"CMD","/C",command};
        }
    }
    
    public BufferedReader executeCommandSystem(String command){
        BufferedReader reader = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(this.systemCommand(command));
            pb.redirectErrorStream(true);
            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            
            pb.directory(new File(System.getProperty("user.home")));
            
            process = pb.start();
            
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            process.waitFor();
            
        } catch (Exception ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reader;
    }
    
}
