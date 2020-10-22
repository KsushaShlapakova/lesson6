import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        String message = "Вы неверно заполнили данные.\n"+
                "Введите их в следующем виде: dd/MM/yyyy идентификатор.\n"+
                "Пример: 21/02/2019 USD";
        try {
            Manager manager = new Manager(args[0], args[1]);

            if(manager.parser().equals("")){
                System.out.println(message);
            }else {
                System.out.println(manager.parser());
            }

        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(message);
        }catch (IOException | SAXException | ParserConfigurationException e){
            e.printStackTrace();
        }
    }
}
