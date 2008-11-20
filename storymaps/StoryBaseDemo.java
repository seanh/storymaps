package storymaps;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryBase class.
 * 
 * @author seanh
 */
public class StoryBaseDemo extends PFrame {

    private StoryBase base;
        
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        base = new StoryBase("Story Base");
        getCanvas().getLayer().addChild(base.getRoot());
        for (int i = 0; i < 31; i++) {
            PPath rect = PPath.createRoundRectangle(0, 0, 200, 250,30,30);
            base.addToGrid(rect);
        }
    }
    
    public static void main(String args[]) {
        StoryBaseDemo demo = new StoryBaseDemo();
    }
}