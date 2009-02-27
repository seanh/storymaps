package storymaps;

import java.awt.Image;

/**
 * Just a passive class that holds information about a Propp function.
 * 
 * @author seanh
 */
public class Function implements Comparable {

    private int number;
    private String propp_name;
    private String friendly_name;
    private String description;
    private String friendly_description;
    private String image_path;
    private Image image;
    
    public Function(int number, String propp_name, String friendly_name,
                    String description, String friendly_description,
                    String image_path) {
        this.number = number;
        this.propp_name = propp_name;
        this.friendly_name = friendly_name;
        this.description = description;
        this.friendly_description = friendly_description;
        this.image_path = image_path;
        this.image = ResourceLoader.loadImage(image_path);
    }
    
    public int getNumber() { return number; }
    public String getProppName() { return propp_name; }
    public String getFriendlyName() { return friendly_name; }
    public String getDescription() { return description; }
    public String getFriendlyDescription() { return friendly_description; }
    public Image getImage() { return image; }
    public String getImagePath() { return image_path; }
    
    @Override
    public String toString() {
        return friendly_name + " (" + friendly_description + " )";
    }
    
    /**
     * Return true if obj is equivalent to this function, false otherwise.
     * 
     * (This is in place of overriding equals, which is a PITA.)
     */
    public boolean compare(Object obj) {
        if (!(obj instanceof Function)) {
            return false;
        } else {
            Function f = (Function) obj;
            // A function's symbol is supposed to uniquely identify that
            // function, so we just check if the symbol's are the same.
            return f.number == this.number;
        }        
    }
    
    /**
     * Implement Comparable.
     */
    public int compareTo(Object arg) {
        Function other = (Function) arg;
        if (other.number > this.number) {
            return 1;
        } else if (other.number == this.number) {
            return 0;
        }
        return -1;
    }

    public static class Memento {

        public int number;
        public String propp_name;
        public String friendly_name;
        public String description;
        public String friendly_description;
        public String image_path;

        public Memento(Function f) {
            this.number = f.getNumber();
            this.propp_name = f.getProppName();
            this.friendly_name = f.getFriendlyName();
            this.description = f.getDescription();
            this.friendly_description = f.getFriendlyDescription();
            this.image_path = f.getImagePath();
        }
        
        @Override
        public String toString() {
            String string = "<div class=\"function\">\n";
            string += "<p>" + number + "\n" + friendly_name + "\n" + friendly_description + "</p>\n";
            string += "<img alt='"+friendly_name+"' src='"+image_path+"'/>\n";
            string += "</div><!--function-->\n";
            return string;
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
            Function f = new Function(m.number, m.propp_name, m.friendly_name,
                    m.description, m.friendly_description, m.image_path);
            return f;
        }
    }
}