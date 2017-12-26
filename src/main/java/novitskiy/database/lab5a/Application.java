package novitskiy.database.lab5a;

import java.sql.*;
import java.util.Scanner;


public class Application {
    private static final String url = "jdbc:mysql://localhost:3306/lab_5";
    private static final String user = "root";
    private static final String password = "0000";

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            readData();
//            updateCoach();
//            insertTrain();
//            deleteCoach();
//            callProcedureForInsertToCoach();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver is not loaded");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
            }

            if (statement != null) try {
                statement.close();
            } catch (SQLException e) {
            }

            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    private static void readData() throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM train");

        System.out.format("\nTable Train ------------------------------------------------------------\n");
        System.out.format("%-10s %s\n", "id_train", "number_train");
        while (resultSet.next()) {
            int idTrain = resultSet.getInt("id_train");
            String numberTrain = resultSet.getString("number_train");
            System.out.format("%-10s %s\n", idTrain, numberTrain);
        }


        resultSet = statement.executeQuery("SELECT * FROM coach");

        System.out.format("\nTable Coach ------------------------------------------------------------\n");
        System.out.format("%-10s %-18s %-12s %-12s %s\n", "id_coach", "number_place", "type_coach", "number_coach", "train_id");
        while (resultSet.next()) {
            int idCoach = resultSet.getInt("id_coach");
            int numberPlace = resultSet.getInt("number_place");
            String typeCoach = resultSet.getString("type_coach");
            String numberCoach = resultSet.getString("number_coach");
            int trainId = resultSet.getInt("train_id");
            System.out.format("%-10s %-18s %-12s %-12s %s\n", idCoach, numberPlace, typeCoach, numberCoach, trainId);
        }


        resultSet = statement.executeQuery("SELECT * FROM ticket");

        System.out.format("\nTable Ticket ------------------------------------------------------------\n");
        System.out.format("%-10s %-15s %-18s %-15s %s\n", "id_ticket", "date_departuere", "place_departuere", "place_arrival", "price_ticket");
        while (resultSet.next()) {
            int idTicket = resultSet.getInt("id_ticket");
            String dateDepartuere = resultSet.getString("date_departuere");
            String placeDepartuere = resultSet.getString("place_departuere");
            String placeArrival = resultSet.getString("place_arrival");
            int priceTicket = resultSet.getInt("price_ticket");

            System.out.format("%-10s %-15s %-18s %-15s %s\n", idTicket, dateDepartuere, placeDepartuere, placeArrival, priceTicket);
        }


//        String query = "SELECT (SELECT number_place FROM coach WHERE id_coach=TC.coach_id) AS coach, " +
//                "(SELECT date_departuere FROM ticket WHERE id_ticket=TC.ticket_id) AS ticket, coach_number " +
//                "FROM ticket_coach AS TC";
//        resultSet = statement.executeQuery(query);
//
//        System.out.format("\nJoining Table Ticket_Coach --------------------\n");
//        System.out.format("%-15s %-15s %s\n", "ticket", "coach", "number");
//        while (resultSet.next()) {
//            int ticketId = resultSet.getInt("ticket_id");
//            int coachId = resultSet.getInt("coach_id");
//            int coachNumber = resultSet.getInt("coach_number");
//            System.out.format("%-15s %-15s %d\n", ticketId, coachId, coachNumber);
//        }

    }

    private static void updateCoach() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input number of coach what you want to update: ");
        String oldNumberCoach = input.next();
        System.out.println("Input new number of coach for %s: " + oldNumberCoach);
        String newNumberCoach = input.next();

        statement.execute("UPDATE coach SET number_coach='" + newNumberCoach + "' WHERE number_coach='" + oldNumberCoach + "';");
    }

    private static void insertTrain() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a new train: ");
        String newTrain = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("INSERT train (number_train) VALUES (?)");
        preparedStatement.setString(1, newTrain);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that inserted: " + n);

    }

    private static void deleteCoach() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a coach for delete: ");
        String coach = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("DELETE FROM coach WHERE number_coach=?");
        preparedStatement.setString(1, coach);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that deleted: " + n);
    }

    private static void callProcedureForInsertToCoach() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\nInput number of place: ");
        String numberPlace = input.next();
        String typeCoach = input.next();
        System.out.println("Input number of coach: ");
        String numberCoach = input.next();
        System.out.println("Input train: ");
        String train = input.next();

        CallableStatement callableStatement;
        callableStatement = connection.prepareCall("{call InsertCoach(?, ?, ?, ?)}");
        callableStatement.setString("number_place_in", numberPlace);
        callableStatement.setString("type_coach_in", typeCoach);
        callableStatement.setString("number_coach_in", numberCoach);
        callableStatement.setString("train_in", train);
        ResultSet resultSet = callableStatement.executeQuery();

        while (resultSet.next()) {
            String msg = resultSet.getString(1);
            System.out.format("\nResult: " + msg);
        }
    }

}
