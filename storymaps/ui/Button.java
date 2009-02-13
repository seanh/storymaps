package storymaps.ui;
import storymaps.Messager;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;


/**
 * A button (clickable PNode) in Piccolo.
 * 
 * @author seanh
 */
public class Button extends PNode {

    /**
     * When clicked a button sends a message (via Messager) with its name.
     */
    private String name;
    
    public Button(String name) {
        this.name = name;
        
        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                clicked();
            }
        });        
    }
    
    protected void clicked() {
        Messager.getMessager().send("button clicked",name);
    }        
}
