import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        String message = "Вы неверно заполнили данные.\n"+
                "Введите их в следующем виде: dd/MM/yyyy идентификатор.\n"+
                "Пример: 21/02/2019 USD \n"+
                "\nЕсли вы хотите обновить базу, введите: updateDB dd/MM/yyyy.\n"+
                "Пример: updateDB 21/02/2019";


        DataBaseManager dbManager = new DataBaseManager();
        APIManager apiManager = new APIManager();

        Valute result;

        try {
            if (!args[0].equals("updateDB")){
                result = dbManager.parser(args[0], args[1]);
                if(result == null){
                    result = apiManager.parser(args[0], args[1]);
                    if (result == null){
                        System.out.println(message);
                    }else{
                        System.out.println(result);
                    }
                }else {
                    System.out.println(result);
                }
            }else{
                String update = dbManager.updateDB(args[1]);
                if(!update.equals("")) {
                    System.out.println(update);
                }else{
                    System.out.println(message);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(message);
        }catch (IOException | SAXException | ParserConfigurationException e){
            e.printStackTrace();
        }
    }
}
