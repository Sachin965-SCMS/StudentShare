package com.notesshared.util;
import java.io.*;import java.nio.file.*;import java.util.*;
public final class UploadUtil{
    private static final long MAX=15L*1024*1024;private UploadUtil(){}
    public static Path uploadDirectory() throws IOException{String c=System.getenv("NOTES_UPLOAD_DIR");Path d=(c==null||c.isBlank()?Paths.get(System.getProperty("user.home"),"StudentShareUploads"):Paths.get(c)).toAbsolutePath().normalize();Files.createDirectories(d);return d;}
    public static String extension(String n){if(n==null)return "";int i=n.lastIndexOf('.');return i<0?"":n.substring(i+1).toLowerCase(Locale.ROOT);}
    public static void validatePdf(String n,long size){if(size<=0)throw new IllegalArgumentException("Please choose a file.");if(size>MAX)throw new IllegalArgumentException("PDF must be 15 MB or smaller.");if(!"pdf".equals(extension(n)))throw new IllegalArgumentException("Only PDF files are accepted in this version.");}
    public static SavedFile save(InputStream in,String original)throws IOException{Path d=uploadDirectory();String stored=UUID.randomUUID()+"."+extension(original);Path target=d.resolve(stored).normalize();if(!target.startsWith(d))throw new IOException("Invalid upload path.");Files.copy(in,target,StandardCopyOption.REPLACE_EXISTING);return new SavedFile(stored,target);}
    public record SavedFile(String storedName,Path path){}
}
