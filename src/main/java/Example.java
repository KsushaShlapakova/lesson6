import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Example {

    public static final String url = "jdbc:mysql://localhost:3306/lesson7";
    public static final String user = "root";
    public static final String pwd = "";


    public static void main(String[] args) {

        String query = "select * from person;";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pwd);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString(2);
                int age = rs.getInt(3);
                System.out.println(name + " " + age);
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
