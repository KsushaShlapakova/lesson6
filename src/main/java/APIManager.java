import kong.unirest.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class APIManager implements Manager {

    List<Valute> currencies = new ArrayList<>();

    public List<Valute> getCurrencies(){
        return currencies;
    }

    @Override
    public Valute parser(String date, String code) throws ParserConfigurationException, IOException, SAXException {

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

        Valute targetValute = null;

        for (Valute val:currencies) {
            if (val.getCharCode().equals(code)) {
                targetValute = val;
            }
        }

        return targetValute;
    }
}
