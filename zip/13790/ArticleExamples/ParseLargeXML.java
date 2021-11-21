import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.ElementPath;
import org.dom4j.ElementHandler;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DOMReader;

public class ParseLargeXML {
	public static void main(String args[])
	{
		try
		{
			ParseLargeXML plXML = new ParseLargeXML();
			plXML.parseLargeXML();

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void parseLargeXML() throws DocumentException, Exception
	{


		SAXReader reader = new SAXReader();
		//birthDays/birthdate
		reader.addHandler( "/birthDays/birthdate",
		    new ElementHandler() {
		        public void onStart(ElementPath path) {
		            // do nothing
		            System.out.println("On Start...");
		        }
		        public void onEnd(ElementPath path) {
		            // process a element
		            System.out.println("On End...");
		            Element row = path.getCurrent();
		            Iterator itr = row.elementIterator();
		            while(itr.hasNext())
		            {
		        		Element child = (Element) itr.next();
		        		//do what ever you want, I will just print
		        	    System.out.println("Element Name:"+child.getQualifiedName() );
						System.out.println("Element Value:"+child.getText());
					}
		            // prune the tree
		            row.detach();
		        }
		    }
		);
		Document doc = reader.read(new File("largetest.xml"));


	}

}