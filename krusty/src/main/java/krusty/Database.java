package krusty;

import spark.Request;
import spark.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
		String sql = "SELECT company AS name, adress AS address FROM customers";
		String customers = "customers";

		return getterSQL(sql,customers);
	}


	public String getRawMaterials(Request req, Response res) {
		String sql = "SELECT ingredient AS name, quantity AS amount, unit FROM inventory";
		String name = "raw-materials";
		return getterSQL(sql,name);
	}


	public String getCookies(Request req, Response res) {
		String sql = "SELECT cookieName as name FROM cookies";
		String name = "cookies";
		return getterSQL(sql,name);
	}


	public String getRecipes(Request req, Response res) {
		String sql = "SELECT * FROM";
		String name = "";
		return getterSQL(sql,name);
	}


	public String getPallets(Request req, Response res) {
		
		String drop = "drop view if  exists palletsView";
		String create = "create view palletsView as SELECT *, IF(blocked, 'yes', 'no') AS block FROM pallets ";
		
		String sql = "SELECT cookieName as cookie, block as blocked FROM palletsView ";
		ArrayList<String> values = new ArrayList<String>();
		boolean firstParamFound = false;

		if (req.queryParams("from") != null) {
			sql += "where prodDate >= ?";
			values.add(req.queryParams("from"));
			firstParamFound = true;
		}

		
		
		if (req.queryParams("to") != null) {
			if(firstParamFound) {
				sql += " and ";
			} else {
				sql +=  " where ";
			}
			sql += "prodDate <=?";
			values.add(req.queryParams("to"));
			
			firstParamFound = true;
		}
		

		if (req.queryParams("cookie") != null) {
			if(firstParamFound) {
				sql += " and ";
			} else {
				sql +=  "where ";
			}
			sql += "cookieName = ?";
		    values.add(req.queryParams("cookie"));
		    
		    System.out.println(sql);
		    
		    firstParamFound = true;
		}
		
		

		if (req.queryParams("blocked") != null) {
			if(firstParamFound) {
				sql += " and ";
			} else {
				sql +=  "where ";
			}
			sql += "block =?";
			values.add(req.queryParams("blocked"));
		}
		
		
		try  {
			PreparedStatement stmt = connection.prepareStatement(sql);
			PreparedStatement stmt1 = connection.prepareStatement(drop);
			PreparedStatement stmt2 = connection.prepareStatement(create);
			for (int i = 0; i < values.size(); i++) {
				stmt.setString(i+1, values.get(i));
			}
			stmt1.executeUpdate();
			stmt2.executeUpdate();
			return Jsonizer.toJson(stmt.executeQuery(),"pallets");


		} catch (SQLException e) {
			e.printStackTrace();
		}


		return "";
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
			System.err.println("Failed to execute reset. The error is "+ e.getMessage());
		}
		return response;
	}


	public String createPallet(Request req, Response res) {
		String response = "{\n\"status\": \"error\"\n}";
		String cookieSQL = "SELECT cookieName FROM cookies WHERE cookieName = ?;";
		try {
			String cookieName = req.queryParams("cookie");
			PreparedStatement stmt = connection.prepareStatement(cookieSQL);
			stmt.setString(1, cookieName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next() == false) {
				return "{\n\"status\": \"unknown cookie\"\n}";
			}
				
			//kolla om rÃ¥varor finns
			Map<String, Integer> recipe = getRecipe(cookieName);
			Map<String, Integer> inventory = getInventory();
			
			for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
			    String recipeIngredient = entry.getKey();
			    int recipeQuantity = entry.getValue() * 54;
			    if(recipeQuantity > inventory.get(recipeIngredient)) {
			    	return "{\n\"status\": \"not enough materials\"\n}";
			    }
			}
			
			//subtrahera frÃ¥n rÃ¥varor
			for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
				String ingredient = entry.getKey();
				int quantity = entry.getValue() * 54;
				String subtractMaterialsSQL = 
						"UPDATE inventory "
						+ "SET quantity = (quantity - ?)"
						+ "WHERE ingredient = ?";
				stmt = connection.prepareStatement(subtractMaterialsSQL);
				stmt.setInt(1, quantity);
				stmt.setString(2, ingredient);
				stmt.executeUpdate();
			}
			
			//skapa pallet
			cookieSQL = "INSERT INTO pallets(cookieName, prodDate) "
					+ "VALUES ((SELECT cookieName FROM cookies WHERE cookieName = ?), now())";
			stmt = connection.prepareStatement(cookieSQL);
			stmt.setString(1, cookieName);
			if(stmt.executeUpdate() == 1) {
				response = "{\n\"status\": \"ok\"\n}";
			}


		} catch (SQLException e) {
			System.err.println("Failed to execute createPallet. The error is "+ e.getMessage());
		}
		return response;
	}


	private String getterSQL(String sql, String name ) {
		String querryResponse = "";
		try (PreparedStatement preStatement = connection.prepareStatement(sql)){
			querryResponse = Jsonizer.toJson(preStatement.executeQuery(),name);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return querryResponse;
	}
	
		private Map<String, Integer> getRecipe(String cookieName) throws SQLException{
		Map <String, Integer> recipe = new HashMap<String, Integer>();
		String sql = "SELECT ingredient, quantity FROM recipes WHERE cookieName = ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, cookieName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
                recipe.put(rs.getString("ingredient"), rs.getInt("quantity"));
            }
		} catch (SQLException e) {
			System.err.println("Failed to execute getRecipe. The error is "+ e.getMessage());
		}
		
		return recipe;
	}
	
	private Map<String, Integer> getInventory() throws SQLException{
		Map <String, Integer> inventory = new HashMap<String, Integer>();
		String sql = "SELECT ingredient, quantity FROM inventory";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
                inventory.put(rs.getString("ingredient"), rs.getInt("quantity"));
            }
		} catch (SQLException e) {
			System.err.println("Failed to execute getRecipe. The error is "+ e.getMessage());
		}
		return inventory;
	}
}
