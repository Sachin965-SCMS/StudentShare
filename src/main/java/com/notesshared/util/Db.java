package com.notesshared.util;
import java.sql.*;
public final class Db {
    private static final String URL=env("NOTES_DB_URL","jdbc:mysql://localhost:3306/notes_shared?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private static final String USER=env("NOTES_DB_USER","root"), PASSWORD=env("NOTES_DB_PASSWORD","password");
    static{try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(ClassNotFoundException e){throw new ExceptionInInitializerError(e);}}
    private Db(){} public static Connection getConnection() throws SQLException{return DriverManager.getConnection(URL,USER,PASSWORD);}
    private static String env(String k,String d){String v=System.getenv(k);return v==null||v.isBlank()?d:v;}
}
