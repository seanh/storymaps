
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

import edu.umd.cs.piccolo.nodes.PImage;
import java.util.logging.Logger;

class Placeholder extends StoryCardBase implements Originator {
            
    private PImage background;
    private boolean taken = false;
    private StoryCard storycard = null;
    
    public Placeholder() {
        super(Function.getFunctions().get(0));
        getNode().addAttribute("Placeholder",this);
        getNode().setVisible(false);
    }
                                     
    public boolean taken() {
        return taken;
    }    
        
    public StoryCard getStoryCard() {
        return storycard;
    }
    
    public void setStoryCard(StoryCard s) {
        if (s == null) {
            clearStoryCard();
        } else {
            s.getNode().addAttribute("Placeholder",this);            
            storycard = s;
            taken = true;
        }
    }
    
    public void clearStoryCard() {
        storycard.getNode().addAttribute("Placeholder",null);
        storycard = null;
        taken = false;
    }

    // Implement Originator
    // --------------------
    
    private static final class PlaceholderMemento implements Memento {
        // No need to defensively copy anything as StoryCardMemento should be
        // immutable.
        private final Memento storyCardMemento;
        PlaceholderMemento (Placeholder p) {
            StoryCard sc = p.getStoryCard();
            if (sc == null) {
                this.storyCardMemento = null;
            } else {
                this.storyCardMemento = sc.createMemento();
            }
        }
        Memento getStoryCardMemento() { return storyCardMemento; }
    }
    
    public Memento createMemento() {
        return new PlaceholderMemento(this);
    }
    
    public static Placeholder newInstanceFromMemento(Memento m) throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Logger.getLogger(Placeholder.class.getName()).throwing("Placeholder", "newInstanceFromMemento", e);
            throw e;
        }
        if (!(m instanceof PlaceholderMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Logger.getLogger(Placeholder.class.getName()).throwing("Placeholder", "newInstanceFromMemento", e);
            throw e;
        }
        PlaceholderMemento pm = (PlaceholderMemento) m;
        Placeholder p = new Placeholder();                
        Memento scm = pm.getStoryCardMemento();
        if (scm != null) {
            p.setStoryCard(StoryCard.newInstanceFromMemento(scm));
        }        
        return p;
    }
}    