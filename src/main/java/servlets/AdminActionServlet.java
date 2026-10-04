/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import utils.DBConnection;

@WebServlet("/AdminAction")
public class AdminActionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int collegeId = (int) request.getSession().getAttribute("college_id");

        try (Connection conn = DBConnection.getConnection()) {
            if ("add_course".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Courses (college_id, course_name, total_semesters) VALUES (?, ?, ?)");
                ps.setInt(1, collegeId);
                ps.setString(2, request.getParameter("course_name"));
                ps.setInt(3, Integer.parseInt(request.getParameter("sems")));
                ps.executeUpdate();
            } else if ("add_subject".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Subjects (course_id, semester_number, subject_name) VALUES (?, ?, ?)");
                ps.setInt(1, Integer.parseInt(request.getParameter("course_id")));
                ps.setInt(2, Integer.parseInt(request.getParameter("sem_num")));
                ps.setString(3, request.getParameter("sub_name"));
                ps.executeUpdate();
            } else if ("approve_student".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("UPDATE Students SET is_approved=1 WHERE student_id=? AND college_id=?");
                ps.setInt(1, Integer.parseInt(request.getParameter("student_id")));
                ps.setInt(2, collegeId);
                ps.executeUpdate();
            }
            response.sendRedirect("admin_dashboard.jsp");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
