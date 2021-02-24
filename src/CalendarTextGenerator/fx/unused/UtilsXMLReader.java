package CalendarTextGenerator.fx.unused;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;

/**
 * @author Lukas 17.03.2017
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class UtilsXMLReader {
    static String readFile(URI filePath) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFac.newDocumentBuilder();
        Document doc = docBuilder.parse(filePath.toString());

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        return result.getWriter().toString();
    }

    public static String readStuff(URI filePath, String stuff) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        String complete = readFile(filePath);

        int firstIndex = complete.indexOf(">", complete.indexOf(stuff));

        return complete.substring(firstIndex+1, complete.indexOf("<", firstIndex));
    }
}
