package com.wyre;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        try {
            File file = new File(System.getProperty("user.dir"));
            for (File fle : file.listFiles()) {
                //skip the file if it is a jar
                if ((!fle.getName().contains(".xml"))) {
                    System.out.println("file is not an xml file, skipping.");
                    continue;
                }
                System.out.println("Processing " + fle.getName());
                // 1- Build the doc from the XML file
                Document doc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(fle);

// 2- Locate the node(s) with xpath
                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodes = (NodeList) xpath.evaluate("//WebView",
                        doc, XPathConstants.NODESET);
//ensure the node list has elements so that we are not processing for nothing
                if (nodes.getLength() <= 0) {
                    System.out.println("file does not have any webviews, skipping");
                    continue;
                }
                System.out.println("file has webviews, replacing.......");
// 3- Make the change on the selected nodes
                for (int idx = 0; idx < nodes.getLength(); idx++) {
                    Node value = nodes.item(idx).getAttributes().getNamedItem("android:layout_width");
                    if(value!=null) {
                        value.setNodeValue("1dp");
                    }
                    Node value2 = nodes.item(idx).getAttributes().getNamedItem("android:layout_height");
                    if(value2!=null) {
                        value2.setNodeValue("1dp");
                    }
                }
//erase the old file
                fle.delete();
// 4- Save the result to a new XML doc
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(new DOMSource(doc), new StreamResult(new File(fle.getAbsoluteFile().toString())));
            }
        } catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        }
    }

