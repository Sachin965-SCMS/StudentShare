package com.notesshared.util;
import jakarta.servlet.http.*;
public final class WebUtil{private WebUtil(){}public static void setFlash(HttpSession s,String t,String m){s.setAttribute("flashType",t);s.setAttribute("flashMessage",m);}public static Integer intParam(HttpServletRequest r,String n){try{String v=r.getParameter(n);return v==null||v.isBlank()?null:Integer.valueOf(v);}catch(NumberFormatException e){return null;}}}
