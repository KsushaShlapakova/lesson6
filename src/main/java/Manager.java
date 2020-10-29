import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Manager {
    Valute parser(String date, String code)
            throws ParserConfigurationException, IOException, SAXException;
}
