<%-- 
    Document   : student_dashboard
    Created on : 24-Sept-2026, 8:31:28?pm
    Author     : Sachin Bobin
--%>

<%@page import="java.sql.*, utils.DBConnection"%>
<% if(session.getAttribute("role") == null || !session.getAttribute("role").equals("student")) response.sendRedirect("auth.jsp"); %>
<!DOCTYPE html>
<html>
<head><title>Student Dashboard</title><link rel="stylesheet" href="css/style.css"></head>
<body>
    <div class="navbar"><h2>Student Space</h2><a href="auth.jsp">Logout</a></div>
    <div class="container flex-grid">
        <div class="card">
            <h3>Upload Material</h3>
            <form action="UploadMaterial" method="POST" enctype="multipart/form-data">
                <select name="subject_id" required>
                    <option value="">Select Subject</option>
                    <% try(Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement("SELECT s.subject_id, s.subject_name, c.course_name, s.semester_number FROM Subjects s JOIN Courses c ON s.course_id = c.course_id WHERE c.college_id=?");
                        ps.setInt(1, (int)session.getAttribute("college_id"));
                        ResultSet rs = ps.executeQuery();
                        while(rs.next()) { %>
                        <option value="<%= rs.getInt("subject_id") %>"><%= rs.getString("course_name") %> - Sem <%= rs.getInt("semester_number") %>: <%= rs.getString("subject_name") %></option>
                    <% }} catch(Exception e){} %>
                </select>
                <select name="material_type" required>
                    <option value="Notes">Hand-written Notes</option>
                    <option value="PDF/PPT">PDF / PPT</option>
                    <option value="Question Paper">Question Paper</option>
                </select>
                <input type="file" name="file" accept=".pdf,.ppt,.pptx,.doc,.docx,.jpg,.png" required>
                <button type="submit">Upload</button>
            </form>
        </div>
        
        <div class="card" style="flex:2;">
            <h3>Available Materials</h3>
            <table>
                <tr>
                    <th>Subject</th>
                    <th>Type</th>
                    <th>Date</th>
                    <th>Action</th>
                </tr>
                <% try(Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("SELECT m.file_path, m.material_type, m.upload_date, s.subject_name FROM Materials m JOIN Subjects s ON m.subject_id = s.subject_id JOIN Courses c ON s.course_id = c.course_id WHERE c.college_id=? ORDER BY m.upload_date DESC");
                    ps.setInt(1, (int)session.getAttribute("college_id"));
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()) { %>
                <tr>
                    <td><%= rs.getString("subject_name") %></td>
                    <td><%= rs.getString("material_type") %></td>
                    <td><%= rs.getTimestamp("upload_date").toString().substring(0, 10) %></td>
                    <td><a href="<%= rs.getString("file_path") %>" target="_blank" style="color:#003366; font-weight:bold;">View/Download</a></td>
                </tr>
                <% }} catch(Exception e){} %>
            </table>
        </div>
    </div>
</body>
</html>