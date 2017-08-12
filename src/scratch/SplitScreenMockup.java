
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
/*
 * Attempt to implement the split-screen mockup from Ray. The entire Piccolo
 * canvas should be divided into a green area at the top and a grey area at the
 * bottom, the two areas filling the entire canvas. The areas should be
 * implemented as two PNodes so they can respond to drag-drops and mouse clicks.
 *
 * I decided to handle window resizes by expanding or shrinking the widths of
 * the two rectangles to match the width of the window. Both rectangles expand
 * and shrink horizontally with the window size. The green rectangle sets its
 * vertical height to as big as it needs to be to contain all of its
 * placeholders. The height of the grey rectangle then expands or shrinks to
 * fill the rest of the vertical space in the window.
 *
 * One problem with this: unlike before, the story cards do not get bigger and
 * smaller as the window gets bigger and smaller. They stay the same size and
 * more empty space appears. So how do you decide the initial size of the story
 * cards? If the user has a bigger screen you want to make use of that by making
 * the story cards bigger so they are more legible. If the user has a smaller
 * screen you want the story cards to be smaller so they fit on screen.
 *
 * I think what is really needed is a combination of this approach with the
 * previous one. As much scaling (or zooming of the camera) as possible should
 * be done, and then the rectangles should stretch to fill in any gaps.
 * 
 */

package scratch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;

/**
 *
 * @author seanh
 */
public class SplitScreenMockup extends JFrame {

    private Container contentPane;
    private PCanvas canvas;
    
    /**
     * The home node, to which all other nodes in the scene are parented.
     */
    private PNode home = new PNode();

    /**
     * The green rectangular container at the top.
     */
    private StoryCardsContainer top;

    /**
     * The color of the top rectangle.
     */
    Color green = new Color(0.75f,0.84f,0.24f);

    /**
     * The grey rectangular container at the bottom.
     */
    private StoryCardsContainer bottom;

    /**
     * The color of the top rectangle.
     **/
    Color grey = new Color(0.66f,0.66f,0.68f);

    SplitScreenMockup() {
        super("Split screen mockup");
        setMinimumSize(new Dimension(800,600));
        setPreferredSize(new Dimension(800,600));

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }
            public void windowOpened(WindowEvent arg0) {
            }
            public void windowClosed(WindowEvent arg0) {
            }
            public void windowIconified(WindowEvent arg0) {
            }
            public void windowDeiconified(WindowEvent arg0) {
            }
            public void windowActivated(WindowEvent arg0) {
            }
            public void windowDeactivated(WindowEvent arg0) {
            }
        });

        canvas = new PCanvas();
        canvas.setFocusable(false); // Never get the keyboard focus.
        contentPane.add(canvas,BorderLayout.CENTER);
        
        pack();

        canvas.getLayer().addChild(home);

        // Instantiate the top and bottom containers.
        float width = (float) canvas.getBounds().getWidth();
        float height = (float) canvas.getBounds().getHeight();
        float height_top_rectangle = height * 0.6f;
        top = new StoryCardsContainer(width,height_top_rectangle,0,0,green,26,9);
        home.addChild(top.getNode());
        bottom = new StoryCardsContainer(width,height-height_top_rectangle,0,height_top_rectangle,grey,26,9);
        home.addChild(bottom.getNode());

        canvas.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent evt) {
                // Resize the rectangles whenever the PCanvas changes size.
                resized();
            }

            public void componentMoved(ComponentEvent arg0) {
            }

            public void componentShown(ComponentEvent arg0) {
            }

            public void componentHidden(ComponentEvent arg0) {
            }
        });

        setVisible(true);
    }

    private void resized(){
        System.out.println("resized");

        // Resize the rectangles so they can fill the new window size.
        double width = canvas.getBounds().getWidth();
        double height = canvas.getBounds().getHeight();
        top.setWidth(width);
        bottom.setWidth(width);
        bottom.setHeight(height-top.getHeight());
        bottom.setOffset(0,top.getHeight());

        // Point the camera at the resized rectangles.
        final PCamera cam = canvas.getCamera();

        //PBounds target = home.getGlobalFullBounds();
        PBounds target = top.getGlobalBounds();
        target.add(bottom.getGlobalBounds());

        cam.animateViewToCenterBounds(target,true,0);
    }

    public static void main(String[] args) {
        new SplitScreenMockup();
    }
}