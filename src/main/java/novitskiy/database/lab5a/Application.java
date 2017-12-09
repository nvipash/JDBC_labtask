package novitskiy.database.lab5a;

import java.sql.*;
import java.util.Scanner;

public class Application {
    private static final String url = "jdbc:mysql://localhost:3306/lab_5a?serverTimezone=UTC&useSSL=false";
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

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver is not loaded");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            //close connection ,statement and resultset
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
            } // ignore
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
//region    SELECT COUNT(*) FROM train //
        // 3. executing SELECT query
        resultSet = statement.executeQuery("SELECT COUNT(*) FROM train");

        // 4. Process the result set
        while (resultSet.next()) {
            int count = resultSet.getInt(1);
            // Simply Print the results
            System.out.format("\ncount: %d\n", count);
        }
        //endregion

//region    SELECT * FROM train //
        // 3. executing SELECT query
        resultSet = statement.executeQuery("SELECT * FROM train");

        // 4. Process the result set
        System.out.format("\nTable Train ------------------------------------------------------------\n");
        System.out.format("%-18s %-18s %-18s %s\n", "id_train", "number_train", "type_train", "photo_train");
        while (resultSet.next()) {
            int idTrain = resultSet.getInt("id_train");
            int numberTrain = resultSet.getInt("number_train");
            String typeTrain = resultSet.getString("type_train");
            String photoTrain = resultSet.getString("photo_train");
            // Simply Print the results
            System.out.format("%-18s %-18s %-18s %s\n", idTrain, numberTrain, typeTrain, photoTrain);
        }
        //endregion

//region    SELECT * FROM coach //
        // 3. executing SELECT query
        resultSet = statement.executeQuery("SELECT * FROM coach");

        // 4. Process the result set
        System.out.format("\nTable Coach ------------------------------------------------------------\n");
        System.out.format("%-18s %-18s %s\n", "id_coach", "number_place", "type_coach");
        while (resultSet.next()) {
            int idCoach = resultSet.getInt("id_coach");
            int numberPlace = resultSet.getInt("number_place");
            String typeCoach = resultSet.getString("type_coach");
            // Simply Print the results
            System.out.format("%-18s %-18s %s\n", idCoach, numberPlace, typeCoach);
        }
        //endregion

//region    SELECT * FROM ticket //
        // 3. executing SELECT query
        resultSet = statement.executeQuery("SELECT * FROM ticket");

        // 4. Process the result set
        System.out.format("\nTable Ticket ------------------------------------------------------------------------------------------------------\n");
        System.out.format("%-12s %-18s %-18s %-15s %-18s %-15s %s\n", "id_ticket", "date_departuere", "place_departuere", "tram_schedule", "place_arrival", "price_ticket", "status_ticket");
        while (resultSet.next()) {
            int idTicket = resultSet.getInt("id_ticket");
            String dateDepartuere = resultSet.getString("date_departuere");
            String placeDepartuere = resultSet.getString("place_departuere");
            String tramSchedule = resultSet.getString("tram_schedule");
            String placeArrival = resultSet.getString("place_arrival");
            int priceTicket = resultSet.getInt("price_ticket");
            String statusTicket = resultSet.getString("status_ticket");
            // Simply Print the results
            System.out.format("%-12s %-18s %-18s %-15s %-18s %-15s %s\n", idTicket, dateDepartuere, placeDepartuere, tramSchedule, placeArrival, priceTicket, statusTicket);
        }
        //endregion

//region    SELECT * FROM ticket_coach //
        // 3. executing SELECT query
        String queryTicketCoach = "SELECT (SELECT status_ticket FROM ticket WHERE id_ticket = ticket_coach.id_ticket_coach) AS ticket, " +
                "(SELECT number_place FROM coach WHERE id_coach = ticket_coach.coach_id) AS coach, number_coach FROM ticket_coach";
        resultSet = statement.executeQuery(queryTicketCoach);

        // 4. Process the result set
        System.out.format("\nJoining Table TicketCoach ------------------------------------------------------------\n");
        System.out.format("%-18s %-15s %s\n", "id_ticket_coach", "coach_id", "number_coach");
        while (resultSet.next()) {
            int idTicketCoach = resultSet.getInt("id_ticket_coach");
            int coachId = resultSet.getInt("coach_id");
            int numberCoach = resultSet.getInt("number_coach");
            // Simply Print the results
            System.out.format("%-18s %-15s %s\n", idTicketCoach, coachId, numberCoach);
        }
        //endregion

    }

    private static void updateDataStatusTicket() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input status of ticket what you want to update: ");
        String statusTicketOld = input.next();
        System.out.println("Input new status of ticket for %s: " + statusTicketOld);
        String statusTicketNew = input.next();

        statement.execute("UPDATE ticket SET status_ticket=" + statusTicketNew + "' WHERE status_ticket='" + statusTicketOld + "';");
    }

    private static void insertDataPlaceDepartuere() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a new place departuere: ");
        String newPlaceDepartuere = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("INSERT ticket VALUES (?)");
        preparedStatement.setString(1, newPlaceDepartuere);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that inserted: " + n);

    }

    private static void insertDataPlaceArrival() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a new place arrival: ");
        String newPlaceArrival = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("INSERT ticket VALUES (?)");
        preparedStatement.setString(1, newPlaceArrival);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that inserted: " + n);

    }

    private static void deleteDataPlaceDepartuere() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a departuere place for delete: ");
        String placeDepartuere = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE place_departuere=?");
        preparedStatement.setString(1, placeDepartuere);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that deleted: " + n);
    }

    private static void deleteDataPlaceArrival() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("Input a arrival place for delete: ");
        String placeArrival = input.next();

        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE place_arrival=?");
        preparedStatement.setString(1, placeArrival);
        int n = preparedStatement.executeUpdate();
        System.out.println("Count rows that deleted: " + n);
    }

    private static void CallProcedureForInsertToTicketCoach() throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.println("\nInput status of ticket for ticket: ");
        String statusTicket = input.next();
        System.out.println("Input number of place for coach: ");
        String numberPlace = input.next();

        CallableStatement callableStatement;
        callableStatement = connection.prepareCall("{call InsertStatusTicket(?, ?)}");
        callableStatement.setString("StatusTicketIn", statusTicket);
        callableStatement.setString("NumberPlaceCoachIN", numberPlace);
        ResultSet rs = callableStatement.executeQuery();

        while (rs.next()) {
            String msg = rs.getString(1);
            // Simply Print the results
            System.out.format("\nResult: " + msg);
        }
    }

}