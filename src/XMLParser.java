import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

public class XMLParser {
	public void parse() {
		SystemInfomation info = SystemInfomation.getInstance();
		//example of adding server to server collection class
		info.addServerInfomation("Big", 3, 3, 1.0f, 1, 1, 1);
//		try {
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.parse(new File("ds-system.xml"));
//
//			XPathFactory xPathfactory = XPathFactory.newInstance();
//			XPath xpath = xPathfactory.newXPath();
//			XPathExpression expr = xpath.compile("//system/servers/server[@type]");
//			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
//
//			for (int temp = 0; temp < nl.getLength(); temp++) {
//				Node currentItem = nl.item(temp);
//				String type = currentItem.getAttributes().getNamedItem("type").getNodeValue();
//				String limit = currentItem.getAttributes().getNamedItem("limit").getNodeValue();
//				String bootupTime = currentItem.getAttributes().getNamedItem("bootupTime").getNodeValue();
//				String hourlyRate = currentItem.getAttributes().getNamedItem("hourlyRate").getNodeValue();
//				String coreCount = currentItem.getAttributes().getNamedItem("coreCount").getNodeValue();
//				String memory = currentItem.getAttributes().getNamedItem("memory").getNodeValue();
//				String disk = currentItem.getAttributes().getNamedItem("disk").getNodeValue();
//				System.out.println(type);
//				System.out.println(limit);
//				System.out.println(bootupTime);
//				System.out.println(hourlyRate);
//				System.out.println(coreCount);
//				System.out.println(memory);
//				System.out.println(disk);
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
