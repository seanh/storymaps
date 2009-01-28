package storymaps;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomReader;

/**
 * A singleton class that reads the functions.xml file and returns a list of
 * Function objects, one for each function in functions.xml, via the
 * getFunctions() static method.
 *  
 * @author seanh
 */
public class Functions {

    /**
     * The XStream instance used to read and write XML.
     */
    private static XStream xstream = new XStream();
    
    /**
     * The single object-instance of Functions.
     */
    private static Functions f = new Functions();    
    
    /**
     * The list of function objects held by the singleton object-instance.
     */
    private ArrayList<Function> functions = new ArrayList<Function>();
    
    /**
     * Return the list of functions.
     */
    public static ArrayList<Function> getFunctions() {
        return f.functions;
    }
    
    /**
     * Construct the singleton Functions instance, parse the functions.xml file
     * and instantiate the Function objects.
     */
    private Functions() {
        parse_xml();            
    }
    
    /**
     * Parse the functions.xml file, instantiate Function objects, and add them
     * to the list functions.
     * 
     */     
    private void parse_xml() {
        // Normally deserializing XML with XStream is very simple, but it strips
        // any newline characters out of text nodes in the XML. To prevent it
        // from stripping newline characters you have to do a lot more work.
        // That's what this is.
        try {
            // Via a series of other classes eventually construct a DomReader
            // object for the functions.xml file.
            InputStream is = this.getClass().getResourceAsStream("/storymaps/data/functions.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.parse(is);
            DomReader domreader = new DomReader(document);
            
            // Pass the DomReader to XStream's unmarshal method to deserialize
            // the XML with newlines in text nodes intact.
            Object o = xstream.unmarshal(domreader);
            
            // Cast the object back to its original type.
            ArrayList<Function.Memento> mementos = (ArrayList<Function.Memento>) o;
            
            // I felt I probably ought to close something at this point.
            is.close();
            
            // Instantiate Function objects from the deserialized list of
            // mementos and add them to the list of functions.
            for (Function.Memento m : mementos) {
                functions.add(Function.newFromMemento(m));
            }
            
        } catch (ParserConfigurationException e) {
            System.out.println(e);
        } catch (SAXException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }        
    
    /**
     * Write the list of functions out to an XML file.
     */
    public static void write_xml() {
        ArrayList<Function.Memento> mementos = new ArrayList<Function.Memento>();
        for (Function function : getFunctions()) {
            mementos.add((Function.Memento)function.saveToMemento());
        }        
        String xml = xstream.toXML(mementos);
        try {
            PrintWriter out = new PrintWriter(new FileWriter("/home/seanh/Desktop/functions.xml"));
            out.print(xml);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException when writing functions.xml" + e.toString());
        }                   
    }            

    public static void main(String args[]) {
        Functions.getFunctions();
    }
}