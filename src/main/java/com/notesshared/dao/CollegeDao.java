package com.notesshared.dao;
import com.notesshared.util.Db;import java.sql.*;
public class CollegeDao{
 public int create(Connection c,String name,String email)throws SQLException{try(PreparedStatement p=c.prepareStatement("INSERT INTO colleges(name,email) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS)){p.setString(1,name);p.setString(2,email);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getInt(1);}}}
 public boolean exists(int id)throws SQLException{try(Connection c=Db.getConnection();PreparedStatement p=c.prepareStatement("SELECT 1 FROM colleges WHERE id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next();}}}
}
