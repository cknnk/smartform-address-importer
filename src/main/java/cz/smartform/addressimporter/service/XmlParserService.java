package cz.smartform.addressimporter.service;

import cz.smartform.addressimporter.dto.ParsedDataDto;
import cz.smartform.addressimporter.entity.CastObce;
import cz.smartform.addressimporter.entity.Obec;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParserService {

    public ParsedDataDto parse(InputStream xmlInputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlInputStream);
        document.getDocumentElement().normalize();

        NodeList obecContainers = document.getElementsByTagNameNS("*", "Obec");
        Element obecElement = null;

        for (int i = 0; i < obecContainers.getLength(); i++) {
            Element el = (Element) obecContainers.item(i);
            if (el.hasAttribute("gml:id") && el.getAttribute("gml:id").startsWith("OB.")) {
                obecElement = el;
                break;
            }
        }

        if (obecElement == null) {
            throw new IllegalStateException("Element <vf:Obec> not found in XML");
        }

        Integer obecKod = Integer.parseInt(getChildTagValue(obecElement, "Kod"));
        String obecNazev = getChildTagValue(obecElement, "Nazev");
        Obec obec = new Obec(obecKod, obecNazev);

        NodeList castNodes = document.getElementsByTagNameNS("*", "CastObce");
        List<CastObce> castiObce = new ArrayList<>();

        for (int i = 0; i < castNodes.getLength(); i++) {
            Element castElement = (Element) castNodes.item(i);

            if (castElement.hasAttribute("gml:id") && castElement.getAttribute("gml:id").startsWith("CO.")) {
                Integer castKod = Integer.parseInt(getChildTagValue(castElement, "Kod"));
                String castNazev = getChildTagValue(castElement, "Nazev");

                Integer parentObecKod = obecKod;
                NodeList obecSubNode = castElement.getElementsByTagNameNS("*", "Obec");
                if (obecSubNode.getLength() > 0) {
                    Element parentEl = (Element) obecSubNode.item(0);
                    parentObecKod = Integer.parseInt(getChildTagValue(parentEl, "Kod"));
                }

                castiObce.add(new CastObce(castKod, castNazev, parentObecKod));
            }
        }

        return new ParsedDataDto(obec, castiObce);
    }

    private String getChildTagValue(Element parentElement, String localTagName) {
        NodeList children = parentElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) node;
                if (localTagName.equals(el.getLocalName())) {
                    return el.getTextContent().trim();
                }
            }
        }
        return "";
    }
}