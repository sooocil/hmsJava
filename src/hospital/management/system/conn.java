package hospital.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conn implements AutoCloseable {

    Connection connection;
    Statement statement;


    public conn(){

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management_system", "root", "soocil");
            statement = connection.createStatement();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }


    @Override
    public void close() throws Exception {

    }
}
