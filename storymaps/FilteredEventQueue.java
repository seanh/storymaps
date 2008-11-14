/* 
@author Alan Richmond

*/

package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FilteredEventQueue extends EventQueue {

    @Override
    protected void dispatchEvent(AWTEvent event) {
        try {
            if (!(event instanceof MouseEvent || event instanceof InvocationEvent))
            {
                System.out.println("DispatchingEvent: " + event);
            }
            super.dispatchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        // Debug.handleException(e);
        }
    }
}