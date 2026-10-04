<%-- 
    Document   : admin_dashboard
    Created on : 24-Sept-2026, 8:31:01?pm
    Author     : Sachin Bobin
--%>
<%@page import="java.sql.*, utils.DBConnection"%>
<% if(session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) response.sendRedirect("auth.jsp"); %>
<!DOCTYPE html>
<html>
<head><title>Admin Dashboard</title><link rel="stylesheet" href="css/style.css"></head>
<body>
    <div class="navbar"><h2>Admin Dashboard</h2><a href="auth.jsp">Logout</a></div>
    <div class="container flex-grid">
        <div class="card">
            <h3>Pending Student Approvals</h3>
            <table>
                <tr><th>Name</th><th>Email</th><th>Action</th></tr>
                <% try(Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students WHERE college_id=? AND is_approved=0");
                    ps.setInt(1, (int)session.getAttribute("college_id"));
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()) { %>
                <tr>
                    <td><%= rs.getString("name") %></td>
                    <td><%= rs.getString("email") %></td>
                    <td>
                        <form action="AdminAction" method="POST">
                            <input type="hidden" name="action" value="approve_student">
                            <input type="hidden" name="student_id" value="<%= rs.getInt("student_id") %>">
                            <button type="submit" style="margin:0; padding:5px;">Approve</button>
                        </form>
                    </td>
                </tr>
                <% }} catch(Exception e){} %>
            </table>
        </div>
        <div class="card">
            <h3>Add Course</h3>
            <form action="AdminAction" method="POST">
                <input type="hidden" name="action" value="add_course">
                <input type="text" name="course_name" placeholder="Course Name (e.g., BCA)" required>
                <input type="number" name="sems" placeholder="Total Semesters" required>
                <button type="submit">Add Course</button>
            </form>
            <hr>
            <h3>Add Subject</h3>
            <form action="AdminAction" method="POST">
                <input type="hidden" name="action" value="add_subject">
                <select name="course_id" required>
                    <% try(Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Courses WHERE college_id=?");
                        ps.setInt(1, (int)session.getAttribute("college_id"));
                        ResultSet rs = ps.executeQuery();
                        while(rs.next()) { %>
                        <option value="<%= rs.getInt("course_id") %>"><%= rs.getString("course_name") %></option>
                    <% }} catch(Exception e){} %>
                </select>
                <input type="number" name="sem_num" placeholder="Semester Number" required>
                <input type="text" name="sub_name" placeholder="Subject Name" required>
                <button type="submit">Add Subject</button>
            </form>
        </div>
    </div>
</body>
</html>