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

/**
 * Base class for various popup dialogs implemented in Piccolo.
 * 
 */
public abstract class Popup extends PNode {
    private PCamera camera;
    private Main main;

    public Popup(int width, int height, Main main) {
        setWidth(width);
        setHeight(height);
        this.camera = main.getCamera();
        this.main = main;
        main.centre(this);
        camera.addChild(this);

        initialise();
    }

    protected Main getMain() {
        return main;
    }

    public abstract void initialise();

    @Override
    public void paint(PPaintContext aPaintContext) {
        Graphics2D g = aPaintContext.getGraphics();

        g.setColor(Colours.BACKGROUND_POPUP);
        g.fill(getBounds().getBounds2D());

        g.setColor(Color.darkGray);
        g.draw3DRect(0, 0, (int)getWidth(), (int)getHeight(), true);

        drawContents(g);
    }

    public abstract void drawContents(Graphics2D g);

    public void dispose() {
        camera.removeChild(this);
    }
}
