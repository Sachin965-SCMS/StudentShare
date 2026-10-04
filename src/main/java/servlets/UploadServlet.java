/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import utils.DBConnection;

@WebServlet("/UploadMaterial")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 100)
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uploaderId = (int) request.getSession().getAttribute("student_id");
        int subjectId = Integer.parseInt(request.getParameter("subject_id"));
        String type = request.getParameter("material_type");

        Part filePart = request.getPart("file");
        
        // Safely extract just the file name, ignoring any client-side paths sent by older browsers
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Define the upload directory safely
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // FIX: Read the input stream directly to bypass GlassFish's buggy Part.write() path resolution
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(uploadPath, fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h3>File System Error:</h3><p>" + e.getMessage() + "</p>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Materials (subject_id, uploader_id, material_type, file_path) VALUES (?, ?, ?, ?)");
            ps.setInt(1, subjectId);
            ps.setInt(2, uploaderId);
            ps.setString(3, type);
            ps.setString(4, "uploads/" + fileName);
            ps.executeUpdate();
            
            response.sendRedirect("student_dashboard.jsp?msg=Upload Success");
        } catch (Exception e) { 
            e.printStackTrace();
            response.setContentType("text/html");
            response.getWriter().println("<h3>Database Error:</h3><p>" + e.getMessage() + "</p>");
        }
    }
}