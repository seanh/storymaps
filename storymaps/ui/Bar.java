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
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.util.PPaintContext;

import java.awt.*;

public class Bar extends PNode {
    public static final int HEIGHT = 70; // non-negotiable
    public static int WIDTH = 1000; // depends on screen rez

    public static final int TEXT_HEIGHT = 42;

    private PCamera camera;
    
    private Main main;
    
    private HelpButton helpButton;

    public Bar(final Main main) {
        this.main = main;
        this.camera = main.getCanvas().getCamera();
        helpButton = new HelpButton(main);        
        addChild(helpButton);
        resize();                                        
    }
    
    public void resize() {
        WIDTH = main.getCanvas().getWidth();
        setWidth(WIDTH);
        setHeight(HEIGHT);
        helpButton.setOffset(WIDTH - 87, TEXT_HEIGHT);        
    }

    @Override
	public void paint(PPaintContext aPaintContext) {
		Graphics2D g = aPaintContext.getGraphics();

        g.setStroke(Strokes.BASIC_FIXED_STROKE);
        g.setColor(Colours.BAR_BACKGROUND);
        g.fill(getBounds().getBounds2D());

        g.setColor(Color.GRAY);
        g.drawRect(0,0,WIDTH-1, HEIGHT-1);

        g.setColor(Colours.FOREGROUND);
        g.setFont(Fonts.LARGE);

        // draw logo
        g.drawString("ZoomDesk", 20, TEXT_HEIGHT);
    }
}