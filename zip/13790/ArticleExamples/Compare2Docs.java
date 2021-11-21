import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DOMReader;
import org.dom4j.util.NodeComparator;

public class Compare2Docs {
	public static void main(String args[])
	{
		try
		{
			Compare2Docs c2d = new Compare2Docs();
			Document d1 = c2d.parseUsingSAX("test.xml");
			Document d2 = c2d.parseUsingSAX("test1.xml");
			NodeComparator comparator = new NodeComparator();
			if ( comparator.compare( d1, d2 ) == 0 ) {
			        System.out.println("Both documents are same.");
			}
			else
			{
					System.out.println("Both documents are different.");
			}


		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Document parseUsingSAX(String fileName) throws DocumentException, Exception
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		return document;
	}

}