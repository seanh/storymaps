package storymaps;
import storymaps.ui.Fonts;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;

/**
 * Absract base class for StoryCard and DisabledStoryCard.
 * @author seanh
 */
abstract class StoryCardBase {
    
    // This story card's Propp function.
    private Function function;
    
    // The card-shaped background geometry of the story card, always visible.       
    private PNode background;
    
    // A story card has two levels of detail, high and low. When in high detail
    // highDetailNode is attached to the background node, when in low detail
    // lowDetailNode is attached.    
    private VerticalLayoutNode highDetailNode;
    private VerticalLayoutNode lowDetailNode;
            
    StoryCardBase(Function function) {

        this.function = function;
        
        background = PPath.createRoundRectangle(-100, -120, 200, 240,20,20);
        background.setPaint(Color.WHITE);
        background.addAttribute("StoryCardBase",this);
                       
        highDetailNode = new VerticalLayoutNode(10);
        highDetailNode.setOffset(-98,-118);
        lowDetailNode = new VerticalLayoutNode(10);
        lowDetailNode.setOffset(-98,-118);        
        
        PText lowDetailTitle = makeTitleNode();
        lowDetailTitle.setScale(2.3);        
        lowDetailNode.addChild(lowDetailTitle);

        PText highDetailTitle = makeTitleNode();
        highDetailTitle.setScale(2);
        highDetailNode.addChild(highDetailTitle);
        
        PImage lowDetailImage = new PImage(function.getImage());
        lowDetailImage.setScale(1.6);
        lowDetailNode.addChild(lowDetailImage);

        PImage highDetailImage = new PImage(function.getImage());
        highDetailImage.setScale(1);
        highDetailNode.addChild(highDetailImage);
        
        HTMLNode descriptionNode = new HTMLNode(function.getDescription());
        descriptionNode.setBounds(0,0,196,100);
        descriptionNode.setFont(Fonts.LARGE);
        highDetailNode.addChild(descriptionNode);

        goToLowDetail();                
    }

    private PText makeTitleNode() {
        PText titleNode = new PText(function.getName());        
        // The font size is really set by the scale of the node, not by font.        
        titleNode.setFont(Fonts.SMALL);
        titleNode.setConstrainWidthToTextWidth(false);
        // Uncomment this line to clip the text if its height goes beyond the
        // bounds.
        //titleNode.setConstrainHeightToTextHeight(false);
        titleNode.setBounds(0, 0, 90, 80);
        return titleNode;
    }
    
    protected PNode getBackground() {return background;}
        
    // FIXME: should use a state-machine for these state changes.
    protected void goToHighDetail() {
        lowDetailNode.removeFromParent();
        background.addChild(highDetailNode);
        background.setChildrenPickable(false); // Just to make sure.
    }
    
    void goToLowDetail() {
        highDetailNode.removeFromParent();
        background.addChild(lowDetailNode);
        background.setChildrenPickable(false); // Just to make sure.
    }    
    
    PNode getNode() {
        return background;
    }
    
    String getTitle() {
        return function.getName();
    }
    
    String getDescription() {
        return function.getDescription();
    }
    
    Function getFunction() {
            return function;
    }               
}