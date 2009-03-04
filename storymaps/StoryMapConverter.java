package storymaps;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;

/**
 * A class that handles converting StoryMap objects to and from XML for XStream.
 * 
 * @author seanh
 */
class StoryMapConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(StoryMap.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
            StoryMap m = (StoryMap) value;
            writer.startNode("title");
            writer.setValue(m.getEditor().getTitle());
            writer.endNode();
            for (Placeholder p : m.getPlaceholders()) {
                writer.startNode("Placeholder");
                context.convertAnother(p);
                writer.endNode();
            }            
       }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
            ArrayList<Placeholder> p = new ArrayList<Placeholder>();
            String title = null;
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                if (reader.getNodeName().equals("title")) {
                    title = reader.getValue();              
                } else if (reader.getNodeName().equals("Placeholder")) {
                    p.add((Placeholder)context.convertAnother(null,Placeholder.class));
                }
                reader.moveUp();
            }
            if (title == null) {
                throw new ConversionException("<StoryMap> tag contains no <title> tag.");
            }
            StoryEditor e = Application.getInstance().getStoryEditor();
            StoryMap m = new StoryMap(title,e,p);            
            e.update(m.getStoryCards());
            e.setTitle(m.getTitle());
            return m;
        }
}