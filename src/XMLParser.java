import javax.xml.xpath.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

public class XMLParser {
	public void parse() {
		SystemInfomation info = SystemInfomation.getInstance();
		//example of adding server to server collection class
		//info.addServerInfomation("Big", 3, 3, 1.0f, 1, 1, 1);

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File("ds-system.xml"));

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//system/servers/server[@type]");
			NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

			for (int temp = 0; temp < nl.getLength(); temp++) {
				Node currentItem = nl.item(temp);
				String type = currentItem.getAttributes().getNamedItem("type").getNodeValue();
				String _limit = currentItem.getAttributes().getNamedItem("limit").getNodeValue();
				String _bootupTime = currentItem.getAttributes().getNamedItem("bootupTime").getNodeValue();
				String _hourlyRate = currentItem.getAttributes().getNamedItem("hourlyRate").getNodeValue();
				String _coreCount = currentItem.getAttributes().getNamedItem("coreCount").getNodeValue();
				String _memory = currentItem.getAttributes().getNamedItem("memory").getNodeValue();
				String _disk = currentItem.getAttributes().getNamedItem("disk").getNodeValue();
				int limit=Integer.parseInt(_limit);
				int bootupTime=Integer.parseInt(_bootupTime);
				Float hourlyRate=Float.parseFloat(_hourlyRate);
				int coreCount=Integer.parseInt(_coreCount);
				int memory=Integer.parseInt(_memory);
				int disk=Integer.parseInt(_disk);
				info.addServerInfomation(type, limit, bootupTime, hourlyRate, coreCount, memory, disk);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
