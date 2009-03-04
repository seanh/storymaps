package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A class that handles converting ApplicationMemento objects to and from XML
 * for XStream, by implementing XStream's Converter interface.
 * 
 * @author seanh
 */
class ApplicationMementoConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(ApplicationMemento.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
            ApplicationMemento m = (ApplicationMemento) value;
            writer.startNode("StoryCards");
            context.convertAnother(m.getStoryCards());
            writer.endNode();
            writer.startNode("StoryMap");
            context.convertAnother(m.getStoryMap());
            writer.endNode();            
       }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            StoryMap m = null;
            StoryCards c = null;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("StoryMap")) {
                    m = (StoryMap) context.convertAnother(null,StoryMap.class);
                }
                else if (reader.getNodeName().equals("StoryCards")) {
                    c = (StoryCards) context.convertAnother(null,StoryCards.class);
                }
                reader.moveUp();
            }
            if (m == null) {
                throw new ConversionException("<ApplicationMemento> tag contains no <StoryMap> tag.");
            }
            if (c == null) {
                throw new ConversionException("<ApplicationMemento> tag contains no <StoryCards> tag.");
            }
            return new ApplicationMemento(m,c);
        }
}