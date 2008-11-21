package storymaps;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Singleton.
 *  
 * @author seanh
 */
public class Functions {

    /**
     * The single object-instance of messager.
     */
    private static Functions f = new Functions();    

    private ArrayList<Function> functions = new ArrayList<Function>();
    
    /**
     * Return the singleton Functions instance.
     */
    public static ArrayList<Function> getFunctions() {
        return f.getList();
    }
    
    public ArrayList<Function> getList() {
        return functions;
    }
    
    private Functions() {
        try {
            parse_xml();
        } catch (Exception e) {
            System.out.println(e);
        }
        for (Function function : functions) {
            System.out.println(function.propp_name);
        }
    }
    
    private void parse_xml() throws ParserConfigurationException, SAXException,
                                    IOException  {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document document = parser.parse(new File("src/storymaps/data/functions.xml"));
        NodeList rows = document.getElementsByTagName("row");
        int numRows = rows.getLength();
        for (int i=0; i<numRows; i++) {
            String symbol = null;
            String propp_name = null;
            String friendly_name = null;
            String description = null;
            String friendly_description = null;
                                    
            Element row = (Element) rows.item(i);
            NodeList fields = row.getElementsByTagName("field");
            int numFields = fields.getLength();
            for (int j=0; j<numFields; j++) {                
                Element field = (Element) fields.item(j);
                if (field.hasAttribute("name")) {                    
                    String name = field.getAttribute("name");
                    String text = null;
                    NodeList children = field.getChildNodes();
                    int numChildren = children.getLength();
                    for (int k=0; k<numChildren; k++) {
                        Node child = children.item(k);
                        if (child.getNodeType() == Node.TEXT_NODE) {
                            text = child.getNodeValue();
                            break;
                        }
                    }
                    if (name.equals("symbol")) {
                        symbol = text;
                    } else if (name.equals("propp name")) {
                        propp_name = text;
                    } else if (name.equals("friendly name")) {
                        friendly_name = text;
                    } else if (name.equals("description")) {
                        description = text;
                    } else if (name.equals("friendly description")) {
                        friendly_description = text;
                    }                                                                                                                            
                }
            }
            Function function = new Function(symbol,propp_name,friendly_name,description,friendly_description);
            functions.add(function);
        }
    }
    
    public static void main(String args[]) {
        Functions.getFunctions();
    }    
}