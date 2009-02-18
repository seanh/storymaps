package storymaps.ui;

import edu.umd.cs.piccolo.nodes.PImage;
import storymaps.ResourceLoader;

/**
 * A button named "Write Story" that displays some text and changes between two
 * states as it's clicked.
 * 
 * FIXME: implement a generic two-state button superclass.
 * 
 * @author seanh
 */
public class WriteStoryButton extends Button {

    private static final String COLLAPSED_TEXT = "Write Story";
    private static final String UNCOLLAPSED_TEXT = "Go Back";
    private static final PImage COLLAPSED_ICON = new PImage(ResourceLoader.loadImage("/storymaps/data/gtk-go-down.png"));
    private static final PImage UNCOLLAPSED_ICON = new PImage(ResourceLoader.loadImage("/storymaps/data/gtk-go-up.png"));
    
    public WriteStoryButton() {
        super("Write Story","Write Story",COLLAPSED_ICON);        
        setScale(10);        
    }
    
    @Override
    protected void clicked() {
        super.clicked();
        if (getText().equals(COLLAPSED_TEXT)) {
            setText(UNCOLLAPSED_TEXT);
            setIcon(UNCOLLAPSED_ICON);
        } else {
            setText(COLLAPSED_TEXT);
            setIcon(COLLAPSED_ICON);
        }
    }    
}