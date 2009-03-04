package storymaps;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A class that handles converting FunctionEditor objects to and from XML for
 * XStream.
 * 
 * @author seanh
 */
class PlaceholderConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(Placeholder.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
            Placeholder p = (Placeholder) value;
            StoryCard s = p.getStoryCard();
            if (s != null) {
                writer.startNode("StoryCard");
                context.convertAnother(s);
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            Placeholder p = new Placeholder();
            StoryCard storycard = null;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("StoryCard")) {
                    storycard = (StoryCard) context.convertAnother(p, StoryCard.class);
                }
                reader.moveUp();
            }
            if (storycard != null) {
                p.setStoryCard(storycard);
            }
            return p;
        }
}