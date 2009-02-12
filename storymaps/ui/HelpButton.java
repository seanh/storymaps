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

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.util.PPaintContext;

import java.awt.*;

public class HelpButton extends PNode {
    private static int WIDTH = 60;
    private static int HEIGHT = 20;

    public HelpButton(final Main main) {
        setWidth(WIDTH);
        setHeight(HEIGHT);

        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mousePressed(PInputEvent event) {
                WelcomePopup wp = new WelcomePopup(main);
                event.setHandled(true);
            }
        });
    }

    @Override
    public void paint(PPaintContext aPaintContext) {
        Graphics2D g = aPaintContext.getGraphics();
        g.setStroke(Strokes.BASIC_FIXED_STROKE);

        int x = 0;
        int y = 0;
        int w = WIDTH-1;
        int h = HEIGHT-1;

        g.setColor(Colours.BACKGROUND_POPUP);
        g.fillRoundRect(x, y, w, h, 10, 10);

        g.setColor(Colours.BUTTON_BORDER_LIGHT);
        g.drawRoundRect(x, y, w, h, 10, 10);
        g.setColor(Colours.BUTTON_BORDER);
        g.drawRoundRect(x+2, y+2, w-4, h-4, 6, 6);

        g.setColor(Colours.FOREGROUND);
        g.setFont(Fonts.SMALL);
        g.drawString("Help", 17, 14);
    }
}