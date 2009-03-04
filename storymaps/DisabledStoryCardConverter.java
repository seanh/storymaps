package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A class that handles converting DisabledStoryCard objects to and from XML for
 * XStream.
 * 
 * @author seanh
 */
class DisabledStoryCardConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(DisabledStoryCard.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {                        
            DisabledStoryCard dsc = (DisabledStoryCard) value;
            writer.startNode("Function");
            context.convertAnother(dsc.getFunction());
            writer.endNode();
            StoryCard sc = dsc.getStoryCard();
            if (sc != null) {
                writer.startNode("StoryCard");
                context.convertAnother(sc);
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            Function function = null;
            StoryCard sc = null;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("Function")) {
                    function = (Function) context.convertAnother(null, Function.class);
                } else if (reader.getNodeName().equals("StoryCard")) {
                    sc = (StoryCard) context.convertAnother(null, StoryCard.class);
                }
                reader.moveUp();
            }
            if (function == null) {
                throw new ConversionException("<DisabledStoryCard> tag contains no <Function> tag.");
            }
            DisabledStoryCard dsc = new DisabledStoryCard(function);
            if (sc != null) {
                dsc.setStoryCard(sc);
            }
            return dsc;
        }
}