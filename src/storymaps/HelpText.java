
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;

/**
 * Display text message overlays that fade in and fade out.
 * 
 * @author seanh
 */
public class HelpText {

    DecoratorNode background = new DecoratorNode();
    PText text = new PText();
    
    public HelpText() {
        background.setPaint(Color.ORANGE);
        background.setTransparency(0);
        background.setOffset(100,200);
        background.setPickable(false);
        
        text.setScale(2.5);
        text.setConstrainHeightToTextHeight(true);
        text.setConstrainWidthToTextWidth(true);
        text.setPickable(false);
        
        background.addChild(text);
        background.setVisible(false);
    }
    
    public PNode getNode() {
        return background;
    }
    
    public void show(String text) {
        show(text,8000);
    }
        
    public void show(String text, long duration) {
        this.text.setText(text);
        this.text.centerBoundsOnPoint(0,0);
        background.setVisible(true);
        PActivity activity = new PActivity(duration) {            
            @Override
            public long processStep(long currentTime) { 
                long timeTaken = currentTime - getStartTime();
                long timeToGo = getStopTime() - currentTime;
                if (timeTaken < 0 || timeToGo < 0) {
                    // Just in case.
                    terminate();
                    return 1;
                }
                if (timeTaken < 1000) {
                    // fading in
                    float alpha = ((float)timeTaken/1000f)*0.9f;
                    background.setTransparency(alpha);
                } else if (timeToGo < 1000) {
                    // fading out
                    float alpha = ((float)timeToGo/1000f)*0.9f;
                    background.setTransparency(alpha);
                } else {
                    // in- between
                }
                return 1;
            }
                                
            @Override
            public void activityFinished() {
                background.setVisible(false);
                background.setTransparency(0); // Just to be sure.
            }
        };
        background.getRoot().addActivity(activity);
    }
}