package com.djfr34k.matilda;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLHandler {

	public static void CreateSession(String playerName){
		
		Connection con = null;
		PreparedStatement pst = null;

		
		String url = "jdbc:mysql://localhost:3306/matilda";
		String user = "matildasa";
		String password = "sqlpassword";
		Statement st = null;
		ResultSet rs= null;
		int openSessions = 99; //Creating default 99 sessions to throw error if while loop fails
		
		try {
			  //Check If A Session Is Already Open
				con = DriverManager.getConnection(url, user, password);
				st = con.createStatement();
				rs =st.executeQuery("SELECT COUNT(*) FROM Sessions WHERE playerName='"+ playerName +"' AND SessionOpen=true");
				while (rs.next()){
					openSessions = rs.getInt("COUNT(*)");
				}
				if (openSessions == 0)
				{
				//Create Session
			  	con = DriverManager.getConnection(url, user, password);
			  	//NEED TO ATTACH STEAM ID IF playerName EXISTS IN Players
			  	pst = con.prepareStatement("INSERT INTO Sessions (PlayerName, SessionOpen) VALUES(?, ?)");
		        pst.setString(1, playerName);
		        pst.setBoolean(2, true);
		        pst.executeUpdate();
				}
				else{
					Logger lgr = Logger.getLogger(SQLHandler.class.getName());
		            lgr.log(Level.SEVERE,"WARNING STUCK OPEN SESSION FOUND FOR: " + playerName +"!");
				}


        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);

	    } finally {
	            try {
	                if (pst != null) {
	                    pst.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                if (rs != null) {
	                	rs.close();
	                }
	                if (st != null) {
	                	st.close();
	                }

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	                lgr.log(Level.WARNING, ex.getMessage(), ex);
	            }
	    }

	}
	
	public static void CloseSession(String playerName){
		
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs= null;

		int openSessions = 99; //declared at 99 in case while loop doesn't run it will still throw session error
		String url = "jdbc:mysql://localhost:3306/matilda";
		String user = "matildasa";
		String password = "sqlpassword";
		
		try {
			//CHECK IF SESSION OPEN IF NOT WE GOTZ A PROBLEM!
				con = DriverManager.getConnection(url, user, password);
				st = con.createStatement();
				rs =st.executeQuery("SELECT COUNT(*) FROM Sessions WHERE playerName='"+ playerName +"' AND SessionOpen=true");
				while (rs.next()){
					openSessions = rs.getInt("COUNT(*)");
				}
			if (openSessions == 1){
				
			//Write End time and close session
			  	con = DriverManager.getConnection(url, user, password);
			  	//No Need to insert steam id as if player was regged this session the registration will attach steam ID to open sessions
			  	pst = con.prepareStatement("UPDATE Sessions Set SessionEnd=Now(), SessionOpen=false, SessionLength=(TIMESTAMPDIFF(Second, SessionStart,SessionEnd)) WHERE playerName=? AND SessionOpen=true");
		        pst.setString(1, playerName);
		        pst.executeUpdate();
			}
			else{
				Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	            lgr.log(Level.SEVERE,"WARNING MORE THAN ONE SESSION FOUND FOR: " + playerName +"!");
			}
		// Close all the streams

        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	            lgr.log(Level.SEVERE,"Unable to write session!");
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);

	    } finally {
	            try {
	                if (pst != null) {
	                    pst.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                if (rs != null) {
	                	rs.close();
	                }

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	                lgr.log(Level.WARNING, ex.getMessage(), ex);
	            }
	    }

	}

	public static String RegisterPlayer(String playerName, String SteamID){
		
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Statement st = null;
		
		String url = "jdbc:mysql://localhost:3306/matilda";
		String user = "matildasa";
		String password = "sqlpassword";
		
		try {
			//Check if player already exists in database if so just change steam ID
			int Exists = 99;
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs =st.executeQuery("SELECT COUNT(*) FROM Players WHERE playerName='"+ playerName +"'");
			while (rs.next()){
				Exists = rs.getInt("COUNT(*)");
			}
			if (Exists == 0){
			//Add Player to Players table
				con = DriverManager.getConnection(url, user, password);
			  	pst = con.prepareStatement("INSERT INTO Players (PlayerName, SteamID) VALUES(?, ?)");
		        pst.setString(1, playerName);
		        pst.setString(2, SteamID);
		        pst.executeUpdate();
			}
			else if (Exists == 1){
			//Update player in table
				con = DriverManager.getConnection(url, user, password);
			  	pst = con.prepareStatement("Update Players Set SteamID=? Where PlayerName=?");
		        pst.setString(1, SteamID);
		        pst.setString(2, playerName);
		        pst.executeUpdate();
			}
			else {
				return "Oh god the database is broken! Have someone check Players table duplicate for " + playerName;
			}
			//Return added or updated based on exists
			if (Exists == 0){	
				return "Added " + playerName + " to Database, Set SteamID=" + SteamID;
			}
			if (Exists == 1){
				return "Updated " + playerName + " in Database, Set new SteamID=" + SteamID;	
			}
			else {
				return "Oh god the database is broken! Have someone check Players table duplicate for " + playerName;
			}
			

        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
	            return "SQL Error Ask Admin to see server log";
	            
	    } finally {
	            try {
	                if (pst != null) {
	                    pst.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	                if (rs != null) {
	                	rs.close();
	                }
	                if (st != null) {
	                	st.close();
	                }

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(SQLHandler.class.getName());
	                lgr.log(Level.WARNING, ex.getMessage(), ex);
	            }
	    }

	}
	
	
	
	
	
} //class end

