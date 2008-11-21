package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;

public class Placeholder {
            
    private PNode background;
    private boolean taken = false;
    
    public Placeholder() {                
        background = PPath.createRoundRectangle(0, 0, 200, 240,20,20);
        background.setPaint(Color.CYAN);
        background.setTransparency(0);              
        background.addAttribute("Placeholder",this);                
        background.setPickable(false);        
    }
                                 
    public PNode getNode() {
        return background;
    }
    
    public boolean taken() {
        return taken;
    }    
    
    public void setTaken(boolean b) {
        taken = b;
    }
}