package storymaps.ui;

import edu.umd.cs.piccolo.nodes.PImage;
import java.io.IOException;
import storymaps.Util;

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
    private static final String COLLAPSED_ICON_PATH = "/storymaps/icons/arrow_down.png";
    private static final String UNCOLLAPSED_ICON_PATH = "/storymaps/icons/arrow_up.png";    
    private static PImage COLLAPSED_ICON = null;
    private static PImage UNCOLLAPSED_ICON = null;
        
    private static void initialiseImagesIfNecessary() {
        COLLAPSED_ICON = Button.initialiseImageIfNecessary(COLLAPSED_ICON_PATH, COLLAPSED_ICON);
        UNCOLLAPSED_ICON = Button.initialiseImageIfNecessary(UNCOLLAPSED_ICON_PATH, UNCOLLAPSED_ICON);
    }
        
    public WriteStoryButton() {
        super("Write Story","Write Story");
        initialiseImagesIfNecessary();
        setIcon(COLLAPSED_ICON);
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