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
            
    protected Function function;
    protected PNode background;
    protected VerticalLayoutNode vnode;    
    protected PText title_node;
    protected PImage image_node;
    protected PText description_node;
        
    StoryCardBase(Function function) {

        this.function = function;
        
        background = PPath.createRoundRectangle(-100, -120, 200, 240,20,20);
        background.setPaint(Color.WHITE);
        background.addAttribute("StoryCardBase",this);
        
        vnode = new VerticalLayoutNode(10);
        vnode.setOffset(-98,-118);
        background.addChild(vnode);
        
        title_node = new PText(function.getName());        
        // The font size is really set by the scale of the node, not by font.        
        title_node.setFont(Fonts.SMALL);
        title_node.setConstrainWidthToTextWidth(false);
        // Uncomment this line to clip the text if its height goes beyond the
        // bounds.
        //title_node.setConstrainHeightToTextHeight(false);
        title_node.setBounds(0, 0, 90, 80);
        title_node.setScale(2.3);
        vnode.addChild(title_node);

        image_node = new PImage(function.getImage());
        image_node.setScale(1.6);
        vnode.addChild(image_node);
        
        description_node = new PText(function.getDescription());
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
        description_node.setFont(Fonts.NORMAL);
                
        background.setChildrenPickable(false);       
    }

    protected void goToHighDetail() {
        title_node.setScale(2);
        image_node.setScale(1);
        vnode.addChild(description_node);          
    }
    
    void goToLowDetail() {
        title_node.setScale(2.3);
        image_node.setScale(1.6);
        description_node.removeFromParent();
    }    
    
    PNode getNode() {
        return background;
    }
    
    String getTitle() {
        return title_node.getText();
    }
    
    String getDescription() {
        return description_node.getText();
    }
    
    Function getFunction() {
            return function;
    }               
}