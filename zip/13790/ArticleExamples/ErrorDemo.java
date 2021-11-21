import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DOMReader;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.*;



public class ErrorDemo {
	public static void main(String args[])
	{
		try
		{
			ErrorDemo errorDemo = new ErrorDemo();
			Document d1 = errorDemo.parseUsingSAX("testerror.xml");



		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Document parseUsingSAX(String fileName)
	{
		XMLErrorHandler errorHandler = new XMLErrorHandler();
		Document document = null;
		SAXReader reader = null;
		try
		{
			reader = new SAXReader();
			reader.setErrorHandler(errorHandler);
			document = reader.read(new File(fileName));
		}catch(Exception e)
		{

			Element root = ((XMLErrorHandler)reader.getErrorHandler()).getErrors();
			// iterate through child elements of root
			for ( Iterator i = root.elementIterator(); i.hasNext(); )
			{
				Element element = (Element) i.next();
				// do something
				System.out.println("Element Name:"+element.getQualifiedName() );
				System.out.println("Element Value:"+element.getText());
			}

		}
		return document;
	}

}