package com.mcgath.rj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** This class is responsible for the creation of the index.html file
 *  that has links to all the entry files. */
public class IndexFile {

    private FileWriter writer;
    
    private final static String fileHead = "<html>\n" +
       "<head><title>%1</title></head>\n" +
       "<body>\n\n" +
       "<h1>%1</h1>" +
       "<ul>";
    
    private final static String fileTail = "</ul>\n" +
        "</body> </html>";
    
    /** Constructor. Sets up a writer and outputs the header.
     * 
     *  @param  parent   The parent directory
     */
    public IndexFile (File parent, String title) throws IOException {
        File ifil = new File (parent, "index.html");
        writer = new FileWriter (ifil);
        String fh1 = fileHead.replace ("%1", title);
        writer.write (fh1);
    }
    
    /** Add an entry to the index file
     *  
     *  @param subject    Subject text, appears as the link name
     *  @param fileName   Name of post file, becomes the link target
     *  */
    public void writeEntry (String subject, String fileName) throws IOException {
        writer.write ("<li>" +
            "<a href=\"" +
            fileName +
            "\">" +
            subject +
            "</a></li>\n");
    }
    
    /** Write the closing HTML tags and close the file. */
    public void close () throws IOException {
        writer.write (fileTail);
        writer.flush ();
        writer.close ();
    }
}
