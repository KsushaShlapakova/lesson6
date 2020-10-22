import kong.unirest.Unirest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class Manager {
    private String date;
    private String code;

    public Manager(String date, String code){
        this.date = date;
        this.code = code;
    }


    public String parser() throws ParserConfigurationException, IOException, SAXException {

        List<Valute> currencies = new ArrayList<>();

         String curr = Unirest.get("http://www.cbr.ru/scripts/XML_daily.asp?date_req={date_req}")
                .routeParam("date_req", date)
                .asString().getBody();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(curr)));

        NodeList names = doc.getDocumentElement().getElementsByTagName("Name");
        NodeList values = doc.getDocumentElement().getElementsByTagName("Value");
        NodeList charCodes = doc.getDocumentElement().getElementsByTagName("CharCode");


        for (int i = 0; i < names.getLength(); i++) {
            currencies.add(new Valute(names.item(i).getTextContent(), values.item(i).getTextContent(),
                    charCodes.item(i).getTextContent()));
        }

        String currName = null;
        String targetValue = null;

        for (Valute val:currencies) {
            if (val.getCharCode().equals(code)) {
                targetValue = val.getValue();
                currName = val.getName();
            }
        }

        if (currName != null && targetValue != null) {
            return "1 " + currName + " = " + targetValue + " Российских рубля.";
        }
        return "";
    }
}
