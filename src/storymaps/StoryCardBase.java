
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
package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Absract base class for StoryCard and DisabledStoryCard.
 * @author seanh
 */
abstract class StoryCardBase {
    
    // The Propp function that this story card represents.
    private Function function;

    // The amount that story card images are scaled by when loaded.
    private static final double SCALE = 0.8;

    private PNode node;
    private SemanticImageNode image;

    StoryCardBase(Function function) {
        this.function = function;
        node = new PNode();
        Image farImage = function.getImage();
        Image nearImage = function.getHighDetailImage();
        image = new SemanticImageNode(farImage, nearImage, SCALE+0.1);
        image.setOffset(-0.5*image.getWidth(),-0.5*image.getHeight());
        image.setScale(SCALE);
        image.setPickable(false);
        node.addChild(image);
        node.setBounds(node.getFullBounds());
        node.addAttribute("StoryCard", this);
    }

    protected PNode getNode() {return node;}

    Image getImage() { return image.getFarImage(); }
    
    String getTitle() { return function.getName(); }
    
    String getDescription() { return function.getDescription(); }
    
    Function getFunction() { return function; }
}