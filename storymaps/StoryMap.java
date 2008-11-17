/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import java.awt.Color;

/**
 *
 * @author seanh
 */
public class StoryMap {
    
    public static final String attribute = "storymap";

    private PNode background;
    
    private VerticalLayoutNode vnode;
            
    private PText title;
    
    private GridLayoutNode grid;
    
    
        
    public StoryMap(String title_string) {

        background = PPath.createRoundRectangle(0, 0, 2200, 1000, 100, 100);
        background.addAttribute(attribute,this);
        background.setPaint(Color.MAGENTA);
                        
        vnode = new VerticalLayoutNode(10);
        vnode.setPickable(false);
        background.addChild(vnode);
        vnode.setOffset(50,25);
        
        title = new PText(title_string);
        title.setScale(10);
        title.setPickable(false);
        vnode.addChild(title);
            
        grid = new GridLayoutNode(10,10);        
        vnode.addChild(grid);        
        grid.setPickable(false);               
    }
    
    public String getTitle() {
        return title.getText();
    }
    
    public void setTitle(String title_string) {
        title.setText(title_string);        
    }
    
    public PNode getNode() {
        return background;
    }
    
    public void addStoryCard(StoryCard s) {
        grid.addChild(s.getNode());    
    }
    
    public PBounds getGlobalFullBounds() {
        return background.getGlobalFullBounds();
    }
}
