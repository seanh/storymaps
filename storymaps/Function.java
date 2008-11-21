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
    
    public Function(String symbol, String propp_name, String friendly_name,
                    String description, String friendly_description) {
        this.symbol = symbol;
        this.propp_name = propp_name;
        this.friendly_name = friendly_name;
        this.description = description;
        this.friendly_description = friendly_description;
    }
    
}
