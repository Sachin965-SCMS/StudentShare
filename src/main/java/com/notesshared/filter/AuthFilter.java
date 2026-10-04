package com.notesshared.filter;
import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.*;
public class AuthFilter implements Filter{public void doFilter(ServletRequest a,ServletResponse b,FilterChain c)throws IOException,ServletException{HttpServletRequest r=(HttpServletRequest)a;HttpServletResponse s=(HttpServletResponse)b;HttpSession h=r.getSession(false);if(h==null||h.getAttribute("userId")==null){s.sendRedirect(r.getContextPath()+"/login.jsp?error=Please+login+to+continue");return;}c.doFilter(a,b);}}
