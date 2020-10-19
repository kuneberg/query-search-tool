package org.kuneberg.qst;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class IbatisXmlParser {


    public Map<String, String> getQueries(InputStream stream) {
        Map<String, String> result = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            {
                NodeList sqlElements = document.getDocumentElement().getElementsByTagName("sql");
                for (int i = 0; i < sqlElements.getLength(); i++) {
                    Node item = sqlElements.item(i);
                    String id = item.getAttributes().getNamedItem("id").getTextContent();
                    String query = item.getTextContent();
                    result.put(id, query);
                }
            }

            {
                NodeList sqlElements = document.getDocumentElement().getElementsByTagName("update");
                for (int i = 0; i < sqlElements.getLength(); i++) {
                    Node item = sqlElements.item(i);
                    String id = item.getAttributes().getNamedItem("id").getTextContent();
                    String query = item.getTextContent();
                    result.put(id, query);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }

}
