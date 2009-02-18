package storymaps.ui;
import storymaps.Messager;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

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
    
    /**
     * The icon that is displayed on the button.
     */
    private PImage icon;
    
    private PNode text_and_icon = new PNode();    
    private PPath outer_rect;
    private PPath inner_rect;
        
    public Button(String name, String text, PImage icon) {
        this.name = name;
        
        addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseClicked(PInputEvent event) {
                clicked();
            }
        });
        
        this.text = new PText(text);
        text_and_icon.addChild(this.text);
        setIcon(icon);        
                
        // Draw two rectangles around the text so it looks like a button.
        float w = (float)text_and_icon.getFullBoundsReference().getWidth() + 7.5f;
        float h = (float)text_and_icon.getFullBoundsReference().getHeight() + 7.5f;        
        outer_rect = PPath.createRoundRectangle(-w/2f,-h/2f,w,h,10,10);
        this.addChild(outer_rect);
        inner_rect = PPath.createRoundRectangle(-w/2f+2,-h/2f+2,w-4,h-4,6,6);
        this.addChild(inner_rect);
                            
        this.addChild(text_and_icon);
    }

    /**
     * Center the text node's geometry at (0,0) in its local coord space/
     */
    private void center_text_and_icon() {
        // Position the icon just to the right of the text.
        icon.setOffset(text.getOffset().getX()+text.getWidth(),text.getOffset().getY());
        // Position the icon and text in the centre of the background rects.
        double width = text_and_icon.getFullBounds().getWidth();
        double height = text_and_icon.getFullBounds().getHeight();
        text_and_icon.setOffset(-width/2f,-height/2f);
    }
    
    protected void clicked() {
        Messager.getMessager().send("button clicked",name);
    }

    public String getName() {
        return name;
    }
        
    public void setText(String text) {
        this.text.setText(text);
        center_text_and_icon();
    }

    public String getText() {
        return text.getText();
    }
        
    public void setIcon(PImage icon) {
        if (this.icon != null) {
            text_and_icon.removeChild(this.icon);
        }        
        // Whatever the original size of the icon, scale it so that it is the
        // same height as the text.        
        icon.setScale(text.getHeight()/icon.getHeight());
        text_and_icon.addChild(icon);
        this.icon = icon;
        center_text_and_icon();
    }    
}