package storymaps;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;

/**
 * Singleton that handles reading and writing XML to file using XStream.
 * 
 * This is the only place where an XStream object is constructed and xstream
 * aliases and converters are set. Many classes throughout the application
 * handle converting class to and from XML for xstream by implementing xstream's
 * Converter interface, these Converter classes are registered with the XStream
 * instance here.
 * 
 * @author seanh
 */
class XMLHandler {

    /**
     * The XStream object used for serializing and desirializing objects to XML.
     */
    private final XStream xstream = new XStream();
    
    /**
     * The Singleton instance of this class.
     */
    private static final XMLHandler INSTANCE = new XMLHandler();
    
    /**
     * Return the singleton instance of this class.
     */
    static XMLHandler getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register xstream converter classes and set xstream aliases. 
     */
    private XMLHandler(){
        xstream.registerConverter(new FunctionConverter());
        xstream.alias("Function", Function.class);
    }    

    /**
     * Write an object out to a file as XML.
     * 
     * Uses XStream and preserves newlines in the XML.
     * 
     * @param o The object to be serialized.
     * @param filename The filename of the XML file to write. Should be a
     * relative filename, e.g. "/storymaps/data/functions.xml". 
     */
    void writeXML(Object o, String filename) throws IOException {                
        String xml = xstream.toXML(o);                                
        Util.writeTextToFile(xml, filename);
    }

    /**
     * Return an object deserialized from an XML file.
     * 
     * @param absolutePath The absolute path to the XML file to read.
     * @return Object The result of deserializing the XML. If an exception
     * occurs while reading and parsing the XML file this method may return
     * null.
     */
    Object readXMLAbsolute(String absolutePath) throws IOException {
        return xstream.fromXML(Util.readTextFromFileAbsolute(absolutePath));                
    }

    /**
     * Return an object deserialized from an XML file
     * 
     * @param relativePath The relative path to the XML file to read, e.g.
     * "/storymaps/data/functions.xml".
     * @return Object The result of deserializing the XML. If an exception
     * occurs while reading and parsing the XML file this method may return
     * null.
     */
    Object readXMLRelative(String relativePath) throws IOException {
        return xstream.fromXML(Util.readTextFromFileRelative(relativePath));                
    }            
}