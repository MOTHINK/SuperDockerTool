/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author mo22
 */
public class Utils {
    public void iconFitInLabel(JLabel label, String iconTextName){
        // Scaling image to fit in JLabel
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + iconTextName));
        label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH)));
    }
    
    public void setInvisible(JLabel label){
        label.setVisible(false);
    }
    
    public void setVisible(JLabel label){
        label.setVisible(true);
    }
    
    public void eraseTextField(List<JTextField> textFields){
        textFields.forEach(x -> x.setText(""));
    }
}
