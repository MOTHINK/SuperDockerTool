/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import files.Filenames;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import utils.AESEncryptionDecryption;

/**
 *
 * @author mo22
 */
public class DockerController {
    private static final String GET_DOCKER_CONTAINERS = "sudo -S docker images | tail -n +2";
    private static final String UNDO_DOCKER = "sudo -S apt-get remove docker docker-engine docker.io containerd runc";
    private static final String GET_DOCKER_VERSION = "sudo -S docker --version";
    private static final String GET_USER_SYSTEM_SH = "whoami";
    
    private static final String CHECK = "sudo -S echos 'hello world'";
    
    private final String sysPwd;
    public boolean isDockerInstalled;
    
    private final CommandExecutor commandExec;
    private final AESEncryptionDecryption aesEncDec;
    
    public DockerController(){
        this.isDockerInstalled = false;
        this.commandExec = new CommandExecutor(this.checkOS());
        this.aesEncDec = new AESEncryptionDecryption(this.getSystemUsername());
        if(this.checkFileExists(Filenames.PASSWORD_FILENAME_PATH)){
            this.sysPwd = "echo " + this.aesEncDec.decrypt(this.getEncSysPwd(Filenames.PASSWORD_FILENAME_PATH)) + "| ";
        } else {
            this.sysPwd = null;
        }
    }
    
    public boolean checkFileExists(String path){
        boolean exists = false;
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) { 
            exists = true;
        }
        return exists;
    }
    
    private boolean deleteFile(String path){
        boolean isDeleted = false;
        File f = new File(path);
        if(f.exists() && !f.isDirectory()){
            f.delete();
            isDeleted = true;
        }
        return isDeleted;
    }
    
    private boolean checkOS(){
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }
    
    private String getSystemUsername(){
        ArrayList<String> output = new ArrayList<String>();
        try {
            BufferedReader reader = this.commandExec.executeCommandSystem(this.sysPwd + this.GET_USER_SYSTEM_SH);
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output.get(0).toString();
    }
    
    
    public ArrayList<String> checkDockerExistsInOS(){
        ArrayList<String> output = new ArrayList<String>();
        try {
            BufferedReader reader = this.commandExec.executeCommandSystem(this.sysPwd + DockerController.GET_DOCKER_VERSION);
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            System.out.println(output);
        } catch (IOException ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    public boolean encryptAndSavePassword(String password){
        boolean isFileCreated = false;        
        try {   
            File f = new File(Filenames.PASSWORD_FILENAME_PATH);
            isFileCreated = f.createNewFile();
            FileWriter fw = new FileWriter(Filenames.PASSWORD_FILENAME_PATH);
            fw.write(aesEncDec.encrypt(password));
            fw.close();
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        
        return isFileCreated;
    }
    
    public String getEncSysPwd(String filePath){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(filePath));   
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
                sb.append(System.lineSeparator());
            } 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString().trim();
    }
    
    public boolean checkIfSystemPasswordIsCorrect(){
       boolean correct = true;
       String output = this.checkDockerExistsInOS().get(0);
       if(output.contains("Sorry, try again.")){
           this.deleteFile(Filenames.PASSWORD_FILENAME_PATH);
           correct = false;
       }
       if(!output.contains("not found")){
           this.isDockerInstalled = true;
       }
       return correct;
    }
    
    
    public boolean uninstallDocker(JTextArea installProcessText){
        boolean deleted = false;
        ArrayList<String> output = new ArrayList<String>();
        try {
            BufferedReader reader = this.commandExec.executeCommandSystem(this.sysPwd + DockerController.GET_DOCKER_CONTAINERS);
            String line;
            while ((line = reader.readLine()) != null) {
                installProcessText.setText(line);
                output.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(DockerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deleted;
    }
    
    public boolean installDocker(){
        boolean isInstaled = false;
        //
        return isInstaled;
    }
    
}
