package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A class that handles converting StoryCard objects to and from XML for
 * XStream.
 * 
 * @author seanh
 */
class StoryCardConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(StoryCard.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
            StoryCard s = (StoryCard) value;
            writer.startNode("Function");
            context.convertAnother(s.getFunction());
            writer.endNode();            
            writer.startNode("text");            
            writer.setValue(s.getEditor().getText());
            writer.endNode();
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            Function function = null;
            String text = null;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("Function")) {
                    function = (Function) context.convertAnother(null, Function.class);
                } else if (reader.getNodeName().equals("text")) {
                    text = reader.getValue();
                }
                reader.moveUp();
            }
            if (function == null) {
                throw new ConversionException("<StoryCard> tag contains no <Function> tag.");
            } else if (text == null) {
                throw new ConversionException("<StoryCard> tag contains no <text> tag.");
            }
            return new StoryCard(function,text);
        }
}