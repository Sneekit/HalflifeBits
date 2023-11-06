import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class PostgresConnector 
{
    Connection connection = null;

    public PostgresConnector()
    {
        openConnection();
    }

	/**
	 * Opens a connection to the postgres database and stores it as a property
	 */
    private void openConnection()
    {
        try 
        {
            // Load database credentials from config file
            InputStream input = new FileInputStream("Config/db.config");
            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("database.url");
            String username = prop.getProperty("database.username");
            String password = prop.getProperty("database.password");

            input.close();

            // Create the connection to the postgres driver
            connection = DriverManager.getConnection(url, username, password);

            // Execute a test query to confirm the connection
            executeQuery("SELECT * FROM decay LIMIT 1;", null);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
    }
	/**
     * Executes a query string with parameters and returns the results
	 * Prevents SQL injection on the parameters
     *
     * @param query			The query string
	 * @param params		The string array of parameters
	 * 
	 * @return the JsonArray of results
	 */
    public JsonArray executeQuery(String query, String[] params)
    {
		// Instantiate the results
        JsonArray results = new JsonArray();

        try
        {
			// test and re-open connection if needed
			if (connection == null || connection.isClosed())
				openConnection();

            PreparedStatement statement = connection.prepareStatement(query);
			statement = addStatementParams(statement, params);

            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

			// Parse results from the result set
            while (resultSet.next()) 
            {
                JsonObject jsonObject = new JsonObject();

                for (int i = 1; i <= columnCount; i += 1) 
                {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(columnName);
                    jsonObject.addProperty(columnName, columnValue);
                }

                results.add(jsonObject);
            }

            resultSet.close();
            statement.close();

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
         
        return results;
    }

	/**
	 * Sets parameters on a statement with SQL injection prevention
	 */
	private PreparedStatement addStatementParams(PreparedStatement statement, String[] params) throws SQLException
	{
		if (params == null)
			return statement;

		for(int i = 0; i < params.length; i += 1)
			statement.setString(i + 1, params[i]);

		return statement;
	}
}