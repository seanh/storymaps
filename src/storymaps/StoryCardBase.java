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
        image = new SemanticImageNode(farImage, nearImage, SCALE);
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