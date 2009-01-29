package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Absract base class for StoryCard and DisabledStoryCard.
 * @author seanh
 */
public abstract class StoryCardBase {
            
    protected Function function;
    protected PNode background;
    protected VerticalLayoutNode vnode;    
    protected PText title_node;
    protected PImage image_node;
    protected PText description_node;
        
    public StoryCardBase(Function function) {

        this.function = function;
        
        background = PPath.createRoundRectangle(-100, -120, 200, 240,20,20);
        background.setPaint(Color.WHITE);
        background.addAttribute("StoryCardBase",this);
        
        vnode = new VerticalLayoutNode(10);
        vnode.setOffset(-98,-118);
        background.addChild(vnode);
        
        title_node = new PText(function.getFriendlyName());        
        title_node.setScale(2.3);
        vnode.addChild(title_node);

        image_node = new PImage(function.getImage());
        image_node.setScale(1.6);
        vnode.addChild(image_node);
        
        description_node = new PText(function.getFriendlyDescription());
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
                
        background.setChildrenPickable(false);       
    }

    protected void goToHighDetail() {
        title_node.setScale(2);
        image_node.setScale(1);
        vnode.addChild(description_node);          
    }
    
    public void goToLowDetail() {
        title_node.setScale(2.3);
        image_node.setScale(1.6);
        description_node.removeFromParent();
    }    
    
    public PNode getNode() {
        return background;
    }
    
    public String getTitle() {
        return title_node.getText();
    }
    
    public String getDescription() {
        return description_node.getText();
    }
    
    public Function getFunction() {
            return function;
    }               
}