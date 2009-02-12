/* ----------------------------------------------------------------------

    This file is part of ZoomDesk (c) Duncan Jauncey 2006

    ZoomDesk is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    ZoomDesk is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with ZoomDesk.  If not, see <http://www.gnu.org/licenses/>.

  ----------------------------------------------------------------------
*/

package storymaps.ui;
import storymaps.Main;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;

public class WelcomePopup extends Popup implements KeyListener, MouseListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;

    public WelcomePopup(Main main) {
        super(WIDTH, HEIGHT, main);
    }

    public void initialise() {
        getMain().getCanvas().addKeyListener(this);
        getMain().getCanvas().addMouseListener(this);
    }

    public void drawContents(Graphics2D g) {
        g.setFont(Fonts.LARGE);
        g.setColor(Colours.FOREGROUND);
        g.drawString("Welcome to Story Maps.", 20, 30);
        g.setFont(Fonts.NORMAL);
        g.drawString("Choose the story cards that you want from the card store at the", 20, 60);
        g.drawString("top.", 20, 80);
        g.drawString("Click and drag them to the story map at the bottom to plan your", 20, 120);
        g.drawString("story.", 20, 140);
        g.drawString("When you're ready to do some writing for your story click on", 20, 180);
        g.drawString("'Write Story'.", 20, 200);
        g.drawString("Remember, if you start writing you can always go back again", 20, 240);
        g.drawString("and change your story map.", 20, 260);
        g.setFont(Fonts.SMALL);
        g.drawString("(please click or press any key to begin)", 130, 280);
        
        getMain().disableHotkeys();
    }

    public void quit() {
        getMain().enableHotkeys();
        getMain().getCanvas().removeKeyListener(this);
        getMain().getCanvas().removeMouseListener(this);
        dispose();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        quit();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        quit();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}