package com.wyre;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

/**
 * This class goes through all xml files in the working directry and resizes the webviews to 1dp to effectivly make them dissapear
 */
public class Main {

    public static void main(String[] args) {
        // get the current directory
        String currdir = System.getProperty("user.dir");
        File fle = new File(currdir);
        for (File file : fle.listFiles()) {
            //if not an xml file then don't process
            if ((!file.isFile()) || !file.getAbsoluteFile().toString().contains(".xml")) {
                System.out.println(file.getName() + "is not an xml file, skipping");
                continue;
            }
            System.out.println("Processing " + file.getName());
            try {

            // 1- Build the doc from the XML file
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(new InputSource(file.getAbsolutePath()));
                //delete the old file
                file.delete();
             // 2- Locate the node(s) with xpath
                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodes = (NodeList) xpath.evaluate("//*[contains(@value, '!Here')]",
                        doc, XPathConstants.NODESET);

             // 3- Make the change on the selected nodes
                for (int idx = 0; idx < nodes.getLength(); idx++) {
                    Node value = nodes.item(idx).getAttributes().getNamedItem("android:layout_width");
                    String val = value.getNodeValue();
                    value.setNodeValue("1dp");
                    Node value2 = nodes.item(idx).getAttributes().getNamedItem("android:layout_height");
                    String val2 = value2.getNodeValue();
                    value2.setNodeValue("1dp");
                }
                //write the new file
                // 4- Save the result to a new XML doc
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(new DOMSource(doc), new StreamResult(new File(file.getAbsoluteFile().toString())));
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
