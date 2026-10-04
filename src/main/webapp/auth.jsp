<%-- 
    Document   : auth
    Created on : 24-Sept-2026, 8:30:25 pm
    Author     : Sachin Bobin
--%>

<%@page import="java.sql.*, utils.DBConnection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>Auth</title><link rel="stylesheet" href="css/style.css"></head>
<body>
    <div class="navbar"><h2>NoteShare Auth</h2><a href="index.jsp">Home</a></div>
    <div class="container flex-grid">
        <div class="card">
            <h3>Login</h3>
            <span style="color:red"><%= request.getParameter("err")!=null ? request.getParameter("err") : "" %></span>
            <form action="AuthServlet" method="POST">
                <input type="hidden" name="action" value="login">
                <select name="role">
                    <option value="student">Student</option>
                    <option value="admin">College Admin</option>
                </select>
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit">Login</button>
            </form>
        </div>
        <div class="card">
            <h3>Register College (Admin)</h3>
            <form action="AuthServlet" method="POST">
                <input type="hidden" name="action" value="admin_register">
                <input type="text" name="cname" placeholder="College Name" required>
                <input type="text" name="contact" placeholder="Contact No" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit">Register College</button>
            </form>
        </div>
        <div class="card">
            <h3>Register Student</h3>
            <form action="AuthServlet" method="POST">
                <input type="hidden" name="action" value="student_register">
                <select name="college_id" required>
                    <option value="">Select Your College</option>
                    <% try (Connection conn = DBConnection.getConnection(); 
                            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Colleges"); 
                            ResultSet rs = ps.executeQuery()) {
                        while(rs.next()) { %>
                            <option value="<%= rs.getInt("college_id") %>"><%= rs.getString("college_name") %></option>
                    <% }} catch(Exception e){} %>
                </select>
                <input type="text" name="sname" placeholder="Full Name" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit">Request Student Account</button>
            </form>
        </div>
    </div>
</body>
</html>