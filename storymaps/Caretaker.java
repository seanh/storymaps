package storymaps;

import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.io.*;

/**
 * Only one Caretaker object is constructed, by Main (although I haven't
 * bothered to explicitly make this class a Singleton). The Caretaker object
 * manages saving and restoring of application state. It maintains a runtime
 * list of saved states, any of which the application can be restored to, which
 * can be used to implement undo and redo. It can also write saved states to
 * file, read them in from file, and export saved states as human-readable plain
 * text (and potentially into other formats as well).
 * 
 * @author seanh
 */
public class Caretaker {

    /**
     * List of saved states of the application. The application can be restored
     * to any of these saved states, or a saved state can be written out to
     * disk and read in again.
     */
    private ArrayList<Object> mementos = new ArrayList<Object>();    

    /**
     * The XStream object that is used for serializing and deserializing memento
     * objects to and from XML.
     */
    private XStream xstream = new XStream();
    
    public Caretaker() {
        xstream.alias("Main",Main.Memento.class);
        xstream.alias("CardStore",StoryCards.Memento.class);
        xstream.alias("DisabledStoryCard",DisabledStoryCard.Memento.class);
        xstream.alias("ProppFunction",Function.Memento.class);
        xstream.alias("StoryCard", StoryCard.Memento.class);
        xstream.alias("StoryMap", StoryMap.Memento.class);
        xstream.alias("Placeholder", Placeholder.Memento.class);
        xstream.alias("FunctionEditor", FunctionEditor.Memento.class);
    }
    
    /**
     * Add a new memento object to the runtime list of saved states.
     */
    public void addMemento(Object m) {
        mementos.add(m);
    }

    /**
     * Get the most recent memento object from the runtime list of saved states.
     */
    public Object getMemento() {
        return getMemento(mementos.size()-1);
    }
    
    /**
     * Get a memento object from the runtime list of saved states.
     */
    public Object getMemento(int index) {
        return mementos.get(index);
    }
    
    /**
     * Write a memento object to a file. Serializes the object to XML using
     * XStream.
     * 
     */
    public void writeMemento(Object m, File f)
    {
        // Use XStream to convert the given memento object to a string of XML.
        String xml = xstream.toXML(m);
        // Add an XML 1.0 declaration and stylesheet reference.
        String head = "";
        try {            
            InputStream is = this.getClass().getResourceAsStream("/storymaps/data/stylesheet.xml");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line=br.readLine()) != null) {
                head = head + line + '\n';
            }
            br.close();
        } catch (IOException e) {
            // Handle IOException
        }
        xml = head + xml + "</doc>";                
        // Then write the string of XML to file.
        try {
            PrintWriter out = new PrintWriter(new FileWriter(f));
            out.print(xml);
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }                        
    }
    
    /**
     * Return a memento object read in from a file. Deserializises the object
     * from XML using XStream.
     * 
     * This method is tightly coupled to the writeMemento method above and to
     * the contents of the stylesheet.xml file read by writeMemento. It uses
     * hardcoded strings to strip out the stylesheet stuff from the XML file
     * and pass only the XML to XStream.
     * 
     * @return The deserialized memento object.
     */
    public Object readMemento(File f) {
        String xml = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));            
            String line;
            boolean passed_stylesheet = false;
            while ((line = in.readLine()) != null) {
                if (passed_stylesheet) {
                    if (line.equals("</doc>")) {
                        continue;
                    } else {
                        xml = xml + line;
                    }
                } else if (line.equals("</xsl:stylesheet>")) {
                    passed_stylesheet = true;                    
                }
            }
            in.close();
        } catch (IOException e) {
            // Handle IOException here.
        }
        Object memento = xstream.fromXML(xml);        
        return memento;
    }
}