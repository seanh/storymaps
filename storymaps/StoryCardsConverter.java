package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;

/**
 * A class that handles converting StoryCards objects to and from XML for
 * XStream.
 * 
 * @author seanh
 */
class StoryCardsConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(StoryCards.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
            StoryCards s = (StoryCards) value;
            writer.startNode("title");
            writer.setValue(s.getTitle());
            writer.endNode();
            for (DisabledStoryCard c : s.getDisabledStoryCards()) {
                writer.startNode("DisabledStoryCard");
                context.convertAnother(c);
                writer.endNode();
            }            
       }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            String title = null;
            ArrayList<DisabledStoryCard> dsc = new ArrayList<DisabledStoryCard>();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("title")) {
                    title = reader.getValue();
                } else if (reader.getNodeName().equals("DisabledStoryCard")) {
                    dsc.add((DisabledStoryCard)context.convertAnother(null,DisabledStoryCard.class));
                }
                reader.moveUp();
            }
            if (title == null) {
                throw new ConversionException("<StoryCards> tag without a <title> tag.");
            }
            return new StoryCards(title,dsc);
        }
}