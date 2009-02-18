package storymaps.ui;
import storymaps.Messager;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;


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
    
    /**
     * The text that is displayed on the button.
     */
    private PText text;
    
    private PPath outer_rect;
    private PPath inner_rect;
        
    public Button(String name) {
        this.name = name;
        
        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                clicked();
            }
        });
        
        text = new PText(name);
        center_text();
        
        // Draw two rectangles around the text so it looks like a button.
        float w = (float)text.getWidth() + 7.5f;
        float h = (float)text.getHeight() + 7.5f;        
        outer_rect = PPath.createRoundRectangle(-w/2f,-h/2f,w,h,10,10);
        this.addChild(outer_rect);
        inner_rect = PPath.createRoundRectangle(-w/2f+2,-h/2f+2,w-4,h-4,6,6);
        this.addChild(inner_rect);
                
        this.addChild(text);        
    }

    /**
     * Center the text node's geometry at (0,0) in its local coord space/
     */
    private void center_text() {
        text.setOffset(-text.getWidth()/2f,-text.getHeight()/2f);
    }
    
    protected void clicked() {
        Messager.getMessager().send("button clicked",name);
    }
    
    public void setText(String text) {
        this.text.setText(text);
        center_text();
    }

    public String getText() {
        return text.getText();
    }
    
    public String getName() {
        return name;
    }
}