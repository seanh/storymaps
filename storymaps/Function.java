package storymaps;

import java.io.Serializable;

/**
 * Just a passive class that holds information about a Propp function.
 * 
 * @author seanh
 */
public class Function {

    // Too dizzy to write accessor methods right now.
    private String symbol;
    private String propp_name;
    private String friendly_name;
    private String description;
    private String friendly_description;
    private String image;
    
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
    
    public String getSymbol() { return symbol; }
    public String getProppName() { return propp_name; }
    public String getFriendlyName() { return friendly_name; }
    public String getDescription() { return description; }
    public String getFriendlyDescription() { return friendly_description; }
    public String getImage() { return image; }
    
    @Override
    public String toString() {
        return friendly_name + " (" + friendly_description + " )";
    }
    
    /**
     * Return true if obj is equivalent to this function, false otherwise.
     */
    public boolean compare(Object obj) {
        if (!(obj instanceof Function)) {
            return false;
        } else {
            Function f = (Function) obj;
            // A function's symbol is supposed to uniquely identify that
            // function, so we just check if the symbol's are the same.
            return f.symbol.equals(this.symbol);
        }        
    }

    private static class Memento implements Serializable {

        public String symbol;
        public String propp_name;
        public String friendly_name;
        public String description;
        public String friendly_description;
        public String image;

        public Memento(Function f) {
            this.symbol = f.getSymbol();
            this.propp_name = f.getProppName();
            this.friendly_name = f.getFriendlyName();
            this.description = f.getDescription();
            this.friendly_description = f.getFriendlyDescription();
            this.image = f.getImage();
        }
    }     
  
    /** Return a memento object for the current state of this originator. */
    public Object saveToMemento() {
        return new Memento(this);
    }

    /** 
     * Return a new Function constructed from a memento object.
     */
    public static Function newFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento.");
        }
        else {
            Memento m = (Memento) o;
            Function f = new Function(m.symbol, m.propp_name, m.friendly_name,
                    m.description, m.friendly_description, m.image);
            return f;
        }
    }
}