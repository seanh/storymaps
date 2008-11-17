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

    private VerticalLayoutNode node;
            
    private PText title;
    
    private GridLayoutNode grid;
    
    public StoryMap(String title_string) {

        node = new VerticalLayoutNode(10);

        title = new PText(title_string);
        title.setScale(10);
        node.addChild(title);
                
                                
        grid = new GridLayoutNode(10,10);        
        node.addChild(grid);
    }
    
    public String getTitle() {
        return title.getText();
    }
    
    public void setTitle(String title_string) {
        title.setText(title_string);
    }
    
    public PNode getNode() {
        return node;
    }
    
    public void addStoryCard(StoryCard s) {
        grid.addChild(s.getNode());    
    }
    
    public PBounds getGlobalFullBounds() {
        return node.getGlobalFullBounds();
    }
}
