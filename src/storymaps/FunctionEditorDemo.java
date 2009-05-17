/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A test class for the FunctionEditor class.
 *
 * @author seanh
 */
public class FunctionEditorDemo {

    private JFrame frame;
    private Container contentPane;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel();
    private JPanel buttons = new JPanel();
    private JButton next = new JButton("Next");
    private JButton prev = new JButton("Previous");

    FunctionEditorDemo() {
        makeFrame();
    }

    private void makeFrame() {
        frame = new JFrame("FunctionEditorDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,300));
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.X_AXIS));

        cardPanel.setLayout(cardLayout);
        for (Function f : Function.getFunctions()) {
            FunctionEditor e = new FunctionEditor(f);
            cardPanel.add(e.getComponent(),f.getName());
        }
        contentPane.add(cardPanel);

        buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(cardPanel);
            }
        });
        prev.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttons.add(prev);
        buttons.add(Box.createVerticalGlue());
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(cardPanel);
            }
        });
        next.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttons.add(next);
        contentPane.add(buttons);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        FunctionEditorDemo demo = new FunctionEditorDemo();
    }    
}