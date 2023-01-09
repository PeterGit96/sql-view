package it.develhope.jdbc;

import it.develhope.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverJDBC {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/newdb";
    private static final String USER = "developer";
    private static final String PASSWORD = "developer1?";

    public static void main(String[] args) {

        Connection conn = null;
        List<Student> italianStudents = new ArrayList<>();
        List<Student> germanStudents = new ArrayList<>();

        try { //main try-block

            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            System.out.printf("Connected to database %s successfully.\n\n", conn.getCatalog());

            Statement statement = conn.createStatement();

            try { //italian view try-block
                statement.executeUpdate("CREATE VIEW italian_students AS SELECT first_name, last_name FROM students WHERE country = 'Italy';");
                System.out.println("Created view italian_student\n");
            } catch(SQLException e){
                System.out.println(e.getMessage() + "\n"); //Table (view) 'italian_students' already exists (if table 'students' exists)
            }

            try { //german view try-block
                statement.executeUpdate("CREATE VIEW german_students AS SELECT first_name, last_name FROM students WHERE country = 'Germany';");
                System.out.println("Created view german_student\n");
            } catch(SQLException e){
                System.out.println(e.getMessage() + "\n"); //Table (view) 'german_students' already exists  (if table 'students' exists)
            }

            ResultSet resultSet = statement.executeQuery("SELECT last_name, first_name FROM italian_students;");

            while (resultSet.next()) {
                italianStudents.add(new Student(resultSet.getString("last_name"), resultSet.getString("first_name")));
            }

            resultSet = statement.executeQuery("SELECT last_name, first_name FROM german_students;");

            while (resultSet.next()) {
                germanStudents.add(new Student(resultSet.getString("last_name"), resultSet.getString("first_name")));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        finally {
            try {
                if(conn != null){
                    conn.close();
                }
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }

        if(!italianStudents.isEmpty()){
            System.out.println("Print all students of italian_students view:\n");
            for (Student student : italianStudents) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }

        if(!germanStudents.isEmpty()){
            System.out.println("\nPrint all students of german_students view:\n");
            for (Student student : germanStudents) {
                System.out.println(student.getLastName() + " " + student.getFirstName());
            }
        }

    }

}
