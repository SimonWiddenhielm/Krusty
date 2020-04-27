package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.mysql.jdbc.Driver;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg11";


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
		String sql = "SELECT * FROM Pallet ";
		
		ArrayList<String> values = new ArrayList<String>();
		
		if (req.queryParams("from") != null) {
		    sql += "where prodDate = ? ";
		    values.add(req.queryParams("from"));
		  }
		
		if (req.queryParams("to") != null) {
		    sql += ...;
		    values.add(req.queryParams("from"));
		}
		
		if (req.queryParams("cookie") != null) {
		    sql += ...;
		    values.add(req.queryParams("from"));
		}
		
		if (req.queryParams("blocked") != null) {
		    sql += ...;
		    values.add(req.queryParams("from"));
		}
		
		String name = "pallets";
		return getterSQL(sql,name);
	}

	public String reset(Request req, Response res) {
		return "{}";
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
	

