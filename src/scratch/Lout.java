package scratch;

import javax.swing.*;
import java.awt.*;

public class Lout {

    Lout() {
        JFrame frame = new JFrame("lout");
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(640,480));

        JLabel north = new JLabel("North");
        contentPane.add(north,BorderLayout.NORTH);
        JLabel east = new JLabel("East");
        contentPane.add(east,BorderLayout.EAST);
        JLabel south = new JLabel("South");
        contentPane.add(south,BorderLayout.SOUTH);
        JLabel west = new JLabel("West");
        west.setAlignmentY(Component.TOP_ALIGNMENT);
        contentPane.add(west,BorderLayout.WEST);
        
        JEditorPane editor = new JEditorPane();
        editor.setContentType("text/html");
        editor.setText("<p><b>vjk</b> fhjkghfjd gghjdfgjdf gjf gjgj kfgjgk fjgkf jgkfgj kfl jgfk gjkf gjfk gjfkf jkgfjgkfjgkfjgkfjg kfjkgfj gkfj gk fjgkf jgf jgfk gj fkgjf kgjfgkj</p> <p>vjk fhjkghfjd gghjdfgjdf gjf gjgj kfgjgk fjgkf jgkfgj kfl jgfk gjkf gjfk gjfkf jkgfjgkfjgkfjgkfjg kfjkgfj gkfj gk fjgkf jgf jgfk gj fkgjf kgjfgkj</p>");

        contentPane.add(editor,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Lout();
    }
}