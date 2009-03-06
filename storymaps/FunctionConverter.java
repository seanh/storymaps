package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A class that handles converting Function objects to and from XML for XStream.
 * 
 * @author seanh
 */
class FunctionConverter implements Converter {

    public boolean canConvert(Class clazz) {
        return clazz.equals(Function.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        Function function = (Function) value;
        writer.startNode("number");
        writer.setValue("" + function.getNumber());
        writer.endNode();
        writer.startNode("proppName");
        writer.setValue(function.getProppName());
        writer.endNode();
        writer.startNode("friendlyName");
        writer.setValue(function.getFriendlyName());
        writer.endNode();
        writer.startNode("description");
        writer.setValue(function.getDescription());
        writer.endNode();
        writer.startNode("friendlyDescription");
        writer.setValue(function.getFriendlyDescription());
        writer.endNode();
        writer.startNode("pathToImage");
        writer.setValue(function.getImagePath());
        writer.endNode();
    }

    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        int number = -1;
        String propp_name = null;
        String friendly_name = null;
        String description = null;
        String friendly_description = null;

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equals("number")) {
                number = Integer.parseInt(reader.getValue());
            } else if (reader.getNodeName().equals("proppName")) {
                propp_name = reader.getValue();
            } else if (reader.getNodeName().equals("friendlyName")) {
                friendly_name = reader.getValue();
            } else if (reader.getNodeName().equals("description")) {
                description = reader.getValue();
            } else if (reader.getNodeName().equals("friendlyDescription")) {
                friendly_description = reader.getValue();
            }
            reader.moveUp();
        }

        if (number == -1) {
            throw new ConversionException("<Function> tag contains no <number> tag (or the <number> tag contains the invalid value -1).");
        }
        if (propp_name == null) {
            throw new ConversionException("<Function> tag contains no <proppName> tag.");
        }
        if (friendly_name == null) {
            throw new ConversionException("<Function> tag contains no <friendlyName> tag.");
        }
        if (description == null) {
            throw new ConversionException("<Function> tag contains no <description> tag.");
        }
        if (friendly_description == null) {
            throw new ConversionException("<Function> tag contains no <friendlyDescription> tag.");
        }
        return new Function(number, propp_name, friendly_name, description,friendly_description);
    }
}