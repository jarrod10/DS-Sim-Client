package Protocol.ConcreteHandler;

import Protocol.Action;
import Protocol.Handler;
import Protocol.Intent;
import Protocol.State;
import Protocol.UnrecognisedCommandException;

public class XMLHandler implements Handler {

    @Override
    public Action enterState() {
//		try {
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document document = builder.parse(new File("ds-system.xml"));
//			
//		    XPathFactory xPathfactory = XPathFactory.newInstance();
//		    XPath xpath = xPathfactory.newXPath();
//		    XPathExpression expr = xpath.compile("//system/servers/server[@type]");
//		    NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
//
//		    for (int temp = 0; temp < nl.getLength(); temp++) {
//		        Node currentItem = nl.item(temp);
//		        String type = currentItem.getAttributes().getNamedItem("type").getNodeValue();
//		        String limit = currentItem.getAttributes().getNamedItem("limit").getNodeValue();
//		        System.out.println(type);
//		    }
//			
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return new Action();
    }


    @Override
    public Action handleMessage(String message) throws UnrecognisedCommandException {
        return new Action(Intent.SWITCH_STATE, State.EVENT_HANDLING);
    }
    
}
