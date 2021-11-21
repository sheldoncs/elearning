
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;



import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DOMReader;

public class LoadWithDOM
{
	public static void main(String[] args)

  	{
	  try
	  {
			LoadWithDOM lWithDOM = new LoadWithDOM();
			org.w3c.dom.Document doc = lWithDOM.loadWithDOM();
			org.dom4j.Document ocDOM=lWithDOM.loadIntoDOM4J(doc);
			lWithDOM.printElements(ocDOM);


	  	}catch(Exception e)
	  	{
		  e.printStackTrace();
	  	}
	}
	public org.w3c.dom.Document loadWithDOM() throws Exception
	{
		//Instantiate a DocumentBuilderFactory.
			DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();

			// And setNamespaceAware, which is required when parsing xsl files
			dFactory.setNamespaceAware(true);

			//Use the DocumentBuilderFactory to create a DocumentBuilder.
			DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
			//Use the DocumentBuilder to parse the XML input.
	        org.w3c.dom.Document xmlDoc = dBuilder.parse("test.xml");
	        return xmlDoc;
	}
	public org.dom4j.Document loadIntoDOM4J(org.w3c.dom.Document doc) throws DocumentException, Exception
	{
				DOMReader reader = new DOMReader();
				org.dom4j.Document document = reader.read(doc);
				return document;
	}
	public void printElements(org.dom4j.Document document) throws DocumentException
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
