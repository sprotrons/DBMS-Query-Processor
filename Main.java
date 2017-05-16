//Original main file before interface builds
//Makes codes on a fixed database

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Main
{
	//Database data
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://localhost:5432/sales";
	static final String user = "postgres";
	static final String password = "supersuper13";
	//Other class
	static final FileToArray fileToArray = new FileToArray();
	static DBMeta DBMeta = new DBMeta();
	static final MakeMF makeMFCode = new MakeMF();
	static Connection connection;
	
	public static void main(String arg[])
	{
		//File path
		String filePath = "./test_examples/example.txt";
		BufferedReader br = null;
		String currentLine;
		try
		{
			br = new BufferedReader(new FileReader(filePath));
			//read file into FileToArray()
			while((currentLine = br.readLine()) != null)
			{
				if(currentLine.equals("SELECT ATTRIBUTE(S):"))
				{
					fileToArray.setSelectAttributes(br, currentLine);
					continue;
				}
				else if(currentLine.equals("NUMBER OF GROUPING VARIABLES(n):"))
				{
					currentLine = br.readLine();
					fileToArray.setGroupingAttrNumber(currentLine);
					continue;
				}
				else if(currentLine.equals("GROUPING ATTRIBUTES(V);"))
				{
					fileToArray.setGroupingAttrs(br, currentLine);
					continue;
				}
				else if(currentLine.equals("F-VECT([F]):"))
				{
					fileToArray.setFV(br, currentLine);
					continue;
				}
				else if(currentLine.equals("SELECT CONDITION-VECT([]):"))
				{
					fileToArray.setConditions(br, currentLine);
					continue;
				}
				else if(currentLine.equals("HAVING CONDITION(G):"))
				{
					fileToArray.setHaving(br, currentLine);
					continue;
				}
			}
			//Deal with conditions.
			
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, user, password);
			checkDBMeta();
			DBMeta.setStructTypeJAVA();
			
			makeMFCode.setClassString(fileToArray, DBMeta);
			
			MakeMain gCode = new MakeMain("./",new InterfaceInfo(), fileToArray, makeMFCode, DBMeta);
			gCode.printGCode();
			connection.close();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	
	//To check metadata for the database
	private static void checkDBMeta()
	{
		String SQLQuery = "select column_name, data_type from information_schema.columns\n"
				+ "where table_name = 'calls'";
		try
		{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(SQLQuery);
			while(rs.next())
			{
				String col = rs.getString("column_name");
				String type = rs.getString("data_type");
				DBMeta.addValue(col, type);
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void printDBMeta(DBMeta DBMeta)
	{
		for(int i = 0; i != DBMeta.getList().size(); i++)
		{
			System.out.println( DBMeta.getList().get(i).getFirst() + "\t"
					+ DBMeta.getList().get(i).getSecond());
		}
	}
}