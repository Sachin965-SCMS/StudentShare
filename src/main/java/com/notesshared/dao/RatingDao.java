package com.notesshared.dao;
import com.notesshared.util.Db;import java.sql.*;
public class RatingDao{public void upsert(int materialId,int studentId,int rating)throws SQLException{String q="INSERT INTO ratings(material_id,student_id,rating) VALUES(?,?,?) ON DUPLICATE KEY UPDATE rating=VALUES(rating),created_at=CURRENT_TIMESTAMP";try(Connection c=Db.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setInt(1,materialId);p.setInt(2,studentId);p.setInt(3,rating);p.executeUpdate();}}}
