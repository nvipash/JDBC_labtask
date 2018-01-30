# JDBC_labtask
Java programm with JDBC (_Java Database Connectivity_) for working with database



#### There are following six steps involved in building a JDBC application:

- Import the packages: Requires that you include the packages containing the JDBC classes needed for database programming. Most often, using import `java.sql.*` will suffice.
- Register the JDBC driver: Requires that you initialize a driver so you can open a communication channel with the database.
- Open a connection: Requires using the `DriverManager.getConnection()` method to create a Connection object, which represents a physical connection with the database.
- Execute a query: Requires using an object of type Statement for building and submitting an SQL statement to the database.
- Extract data from result set: Requires that you use the appropriate `ResultSet.getXXX()` method to retrieve the data from the result set.
- Clean up the environment: Requires explicitly closing all database resources versus relying on the JVM's garbage collection.
