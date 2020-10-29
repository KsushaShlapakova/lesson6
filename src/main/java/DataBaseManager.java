import kong.unirest.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataBaseManager implements Manager {

    public static final String url = "jdbc:mysql://localhost:3306/currencies?useUnicode=true&characterEncoding=utf8";
    public static final String user = "root";
    public static final String pwd = "";

    @Override
    public Valute parser(String date, String code){
        Valute targetValute = null;

        String query = "select * from currency where charcode = '" + code + "' and date = '" + date +"';";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                targetValute = new Valute(rs.getString(3),rs.getString(4), rs.getString(2));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return targetValute;
    }

    public String updateDB(String date){
        String message = "База данных успешно обновлена.";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement st = con.createStatement();

            String check = "select * from currency where date = '" + date + "';";
            ResultSet rs = st.executeQuery(check);

            if (!rs.next()) {
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

                if (!currencies.isEmpty()) {
                    for (Valute val : currencies) {

                        String query = "insert into currency (charcode, name, value, date) values ('" + val.getCharCode() +
                                "', N'" + val.getName() + "', '" + val.getValue() + "', '" + date + "');";

                        st.executeUpdate(query);
                    }
                }else{
                    message = "";
                }
            }else{
                message = "Данные за "+ date+" уже добавлены.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
