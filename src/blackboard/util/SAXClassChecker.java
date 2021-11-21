package blackboard.util;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

class SAXClassChecker {
  public static void main(String[] args) {
     try {
     	 File x = new File("xml/howto.xml");
        SAXParserFactory f = SAXParserFactory.newInstance();
        System.out.println(f.toString());
        SAXParser p = f.newSAXParser();
        System.out.println(p.toString());
        DefaultHandler h = new DefaultHandler();
        System.out.println(h.toString());
        p.parse(x,h);
     } catch (ParserConfigurationException e) {
        System.out.println(e.toString()); 
     } catch (SAXException e) {
        System.out.println(e.toString()); 
     } catch (IOException e) {
        System.out.println(e.toString()); 
     }
  }
}

