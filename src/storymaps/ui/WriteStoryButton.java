
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@lavabit.com>

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
package storymaps.ui;

import edu.umd.cs.piccolo.nodes.PImage;

/**
 * A button named "Write Story" that displays some text and changes between two
 * states as it's clicked.
 * 
 * FIXME: implement a generic two-state button superclass.
 * 
 * @author seanh
 */
public class WriteStoryButton extends Button {

    private static final String COLLAPSED_TEXT = "Write Story";
    private static final String UNCOLLAPSED_TEXT = "Go Back";
    private static final String COLLAPSED_ICON_PATH = "/data/icons/arrow_down.png";
    private static final String UNCOLLAPSED_ICON_PATH = "/data/icons/arrow_up.png";
    private static PImage COLLAPSED_ICON = null;
    private static PImage UNCOLLAPSED_ICON = null;
        
    private static void initialiseImagesIfNecessary() {
        COLLAPSED_ICON = Button.initialiseImageIfNecessary(COLLAPSED_ICON_PATH, COLLAPSED_ICON);
        UNCOLLAPSED_ICON = Button.initialiseImageIfNecessary(UNCOLLAPSED_ICON_PATH, UNCOLLAPSED_ICON);
    }
        
    public WriteStoryButton() {
        super("Write Story","Write Story");
        initialiseImagesIfNecessary();
        setIcon(COLLAPSED_ICON);
        setScale(10);        
    }
    
    @Override
    protected void clicked() {
        super.clicked();
        if (getText().equals(COLLAPSED_TEXT)) {
            setText(UNCOLLAPSED_TEXT);
            setIcon(UNCOLLAPSED_ICON);
        } else {
            setText(COLLAPSED_TEXT);
            setIcon(COLLAPSED_ICON);
        }
    }    
}