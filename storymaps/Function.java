package storymaps;

/**
 * Just a passive class that holds information about a Propp function.
 * 
 * @author seanh
 */
public class Function {

    // Too dizzy to write accessor methods right now.
    public String symbol;
    public String propp_name;
    public String friendly_name;
    public String description;
    public String friendly_description;
    public String image;
    
    public Function(String symbol, String propp_name, String friendly_name,
                    String description, String friendly_description,
                    String image) {
        this.symbol = symbol;
        this.propp_name = propp_name;
        this.friendly_name = friendly_name;
        this.description = description;
        this.friendly_description = friendly_description;
        this.image = image;
    }
    
    /**
     * Return true if obj is equivalent to this function, false otherwise.
     */
    public boolean compare(Object obj) {
        if (!(obj instanceof Function)) {
            return false;
        } else {
            Function f = (Function) obj;
            return f.propp_name.equals(this.propp_name);
        }        
    }
    
    /** Return a memento object for the current state of this originator. */
    public Object saveToMemento() {
        // Since Function is a passive class anyway, we just use the Function
        // object itself as the Memento.
        return this;                
    }

    /** 
     * Return a new Function constructed from a memento object.
     */
    public static Function newFromMemento(Object m) {
        // And this is just a copy-constructor.
        if (!(m instanceof Function)) {
            throw new IllegalArgumentException("Argument not instanceof Function.");
        }
        else {
            Function f = (Function) m;
            return f;
        }
    }
}