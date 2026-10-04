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

@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try (Connection conn = DBConnection.getConnection()) {
            if ("admin_register".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Colleges (college_name, contact_number, email, password) VALUES (?, ?, ?, ?)");
                ps.setString(1, request.getParameter("cname"));
                ps.setString(2, request.getParameter("contact"));
                ps.setString(3, request.getParameter("email"));
                ps.setString(4, request.getParameter("password"));
                ps.executeUpdate();
                response.sendRedirect("auth.jsp?msg=College Registered. Please Login.");
            } 
            else if ("student_register".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Students (college_id, name, email, password) VALUES (?, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(request.getParameter("college_id")));
                ps.setString(2, request.getParameter("sname"));
                ps.setString(3, request.getParameter("email"));
                ps.setString(4, request.getParameter("password"));
                ps.executeUpdate();
                response.sendRedirect("auth.jsp?msg=Registration sent to admin for approval.");
            }
            else if ("login".equals(action)) {
                String role = request.getParameter("role");
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                HttpSession session = request.getSession();

                if ("admin".equals(role)) {
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM Colleges WHERE email=? AND password=?");
                    ps.setString(1, email); ps.setString(2, pass);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        session.setAttribute("college_id", rs.getInt("college_id"));
                        session.setAttribute("role", "admin");
                        response.sendRedirect("admin_dashboard.jsp");
                    } else response.sendRedirect("auth.jsp?err=Invalid Admin Login");
                } else {
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students WHERE email=? AND password=?");
                    ps.setString(1, email); ps.setString(2, pass);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        if (!rs.getBoolean("is_approved")) {
                            response.sendRedirect("auth.jsp?err=Account pending admin approval.");
                        } else {
                            session.setAttribute("student_id", rs.getInt("student_id"));
                            session.setAttribute("college_id", rs.getInt("college_id"));
                            session.setAttribute("role", "student");
                            response.sendRedirect("student_dashboard.jsp");
                        }
                    } else response.sendRedirect("auth.jsp?err=Invalid Student Login");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}