package krusty;

import spark.Request;
import spark.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import com.mysql.jdbc.Driver;
import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg11?allowMultiQueries=true";


	private static final String jdbcUsername = "hbg11";
	private static final String jdbcPassword = "nbo078yk";
	private Connection connection;
	private DriverManager Drivemanager;
	
	public void connect()  {

		
		try {
			connection = DriverManager.getConnection(jdbcString, jdbcUsername,  jdbcPassword);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
		
		
		String sql = "SELECT * FROM Customer";
		String customers = "Customers";
		
		return getterSQL(sql,customers);
	}

	public String getRawMaterials(Request req, Response res) {
		String sql = "SELECT * FROM Ingredient";
		String name = "RawMaterials";
		return getterSQL(sql,name);
	}

	public String getCookies(Request req, Response res) {
		String sql = "SELECT * FROM Cookie";
		String name = "Cookies";
		return getterSQL(sql,name);
	}
		
		

	public String getRecipes(Request req, Response res) {
		
		String sql = "SELECT * FROM";
		String name = "";
		return getterSQL(sql,name);
	}

	public String getPallets(Request req, Response res) {
		String sql = "SELECT * FROM pallets ";
		
		ArrayList<String> values = new ArrayList<String>();
		
		boolean firstParamFound = false;
		
		
		
		//denna funkar
		if (req.queryParams("from") != null) {
		    sql += "where prodDate >= ?";
		    values.add(req.queryParams("from"));
		    firstParamFound = true;
		  }
		
		
		
		//men inte denna??
		
		if (req.queryParams("to") != null) {
			if(firstParamFound) {
				sql += " and ";
			}
		    sql += "where prodDate <= ?";
		    values.add(req.queryParams("to"));
		}
		/*
		
		if (req.queryParams("cookie") != null) {
		    sql += ...;
		    values.add(req.queryParams("from"));
		}
		
		if (req.queryParams("blocked") != null) {
		    sql += ...;
		    values.add(req.queryParams("from"));
		}
		*/

		  try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		    for (int i = 0; i < values.size(); i++) {
		      stmt.setString(i+1, values.get(i));
		    }
		    return Jsonizer.toJson(stmt.executeQuery(),"pallets");
		    
		    
		  } catch (SQLException e) {
			  e.printStackTrace();
		  }
		
		
		String name = "pallets";
		return getterSQL(sql,name);
	}

	public String reset(Request req, Response res) {
		String response = "{\n\"status\": \"error\"\n}";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("reset.sql"));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				sb.append(str + " ");
			}
			reader.close();
			PreparedStatement preStatement = connection.prepareStatement(sb.toString());
			preStatement.executeQuery();
			response = "{\n\"status\": \"ok\"\n}";
		} catch (Exception e) {
			System.err.println("Failed to Execute " + "reset" + ". The error is "+ e.getMessage());
		}
		return response;
	}

	public String createPallet( Request req, Response res) {
		return "{}";
	}
	private String getterSQL(String sql, String name ) {
		String querryResponse = "";
		try {
			PreparedStatement preStatement = connection.prepareStatement(sql);
			querryResponse = Jsonizer.toJson(preStatement.executeQuery(),name);
	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return querryResponse;
	}
		
	}
