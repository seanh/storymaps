package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author seanh
 */
public class Swing {
    
    private JFrame frame;
    
    public Swing() {
        makeFrame();
    }

    private void makeFrame() {
        frame = new JFrame("StoryMaps");
        Container contentPane = frame.getContentPane();
        
        contentPane.setLayout(new BorderLayout());
        
        JLabel label = new JLabel("Ywan");
        contentPane.add(label,BorderLayout.NORTH);
        
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        fileMenu.add(quitItem);
        
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        helpMenu.add(aboutItem);
        
        frame.pack();
        frame.setVisible(true);
    }

    private void openFile() {
        
    }
    
    private void quit() {
        
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
                "StoryMaps",
                "About StoryMaps",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        new Swing();
    }
    
}
