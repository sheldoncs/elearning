import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DOMReader;

public class LoadAndParse {
	public static void main(String args[])
	{
		try
		{
			LoadAndParse landp = new LoadAndParse();
			Document d = landp.parseUsingSAX();
			landp.printElements(d);
			//Document domDoc = landp.parseUsingDOM(d);
			//landp.printElements(d);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Document parseUsingSAX() throws DocumentException, Exception
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File("test.xml"));
		return document;
	}
	/*
	public Document parseUsingDOM(Document doc) throws DocumentException, Exception
	{
			DOMReader reader = new DOMReader();
			Document document = reader.read(doc);
			return document;
	}
	*/
	public void printElements(Document document) throws DocumentException
	{
		Element root = document.getRootElement();
		// iterate through child elements of root
		for ( Iterator i = root.elementIterator(); i.hasNext(); )
		{
			Element element = (Element) i.next();
			// do something
			System.out.println("Element Name:"+element.getQualifiedName() );
			System.out.println("Element Value:"+element.getText());
		}

	}

}