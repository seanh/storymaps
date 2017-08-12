
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
package scratch;

import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;

/**
 * Base class for different types of story card.
 *
 * @author seanh
 */
public class StoryCard extends PNodeWrapper {

    private String title;

    public StoryCard(String title) {
        super(newRectangle());
        this.title = title;
    }

    /**
     * StoryCard exports the method it uses to make the rectangle-shaped
     * background of a story card so that other classes can make story card-
     * shaped objects.
     *
     * @return A rectangular story card shape.
     */
    public static PPath newRectangle() {
        PPath rectangle = PPath.createRoundRectangle(0, 0, 70, 82,8,8);
        Color color = new Color(1.0f,0.9f,0.6f);
        rectangle.setPaint(color);
        rectangle.setStrokePaint(color);
        return rectangle;
    }
}