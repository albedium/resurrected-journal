package com.mcgath.rj;

import java.util.ArrayList;
import java.util.List;

/** A LiveJournal post (or "event") */
public class Post {

    private final static String HTML_TOP = "<html><head>\n" +
                 "<title>NEED TITLE HERE</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                 "</head>\n" +
                 "<body>\n";
    
    private final static String HTML_BOTTOM = "</body></html>";
    
    private List<Comment> comments;
    private String body;
    private String date;
    private String subject;
    private boolean autoFormat;
    
    private String previousFile;
    private String nextFile;
    
    public Post () {
        comments = new ArrayList<Comment> ();
        body = "";
        autoFormat = true;  // assume autoformat as default
        subject = null;
    }

    /** Set the auto-format flag */
    public void setAutoFormat (boolean b) {
        autoFormat = b;
    }
    
    /** Set the previous file for linking
     */
    public void setPreviousFile (String s) {
        previousFile = s;
    }
    
    /** Set the next file for linking
     */
    public void setNextFile (String s) {
        nextFile = s;
    }

    /** Set the subject */
    public void setSubject (String s) {
        subject = s;
        
    }
    /** Set the body text */
    public void setBody (String s) {
        if (autoFormat) {
            body = s.replace("\n", "<p>\n");
        }
        else {
            body = s;
        }
    }
    
    /** Set the date string */
    public void setDate (String s) {
        date = s;
    }
    
    /** Add a comment. Only top-level comments (ones which aren't replies
     *  to other comments) should be added this way. */
    public void addComment (Comment cmt) {
        comments.add (cmt);
    }
    
    /** Returns the subject; may be null */
    public String getSubject () {
        return subject;
    }
    
    /** Returns the comment list, which is a nested structure */
    public List<Comment> getComments () {
        return comments;
    }
    
    /** Return post as the complete content of an HTML file */
    public String toHTML () {
        // TODO very crude
        StringBuffer ret = new StringBuffer (HTML_TOP);
        String subj = (subject == null) ? "(No subject)" : subject;
        ret.append("<h1>" + subj + "</h1>\n");
        ret.append ("Date: " + date + "<br>");
        ret.append(body);
        if (!comments.isEmpty()) {
            ret.append ("<h2>Comments:</h2>\n");
        }
        for (Comment c : comments) {
            ret.append (c.toHTML());
        }
        if (previousFile != null) {
            ret.append ("<br>");
            ret.append ("<a href=\"");
            ret.append (previousFile);
            ret.append ("\">Previous</a>");
        }
        if (nextFile != null) {
            ret.append ("<br>");
            ret.append ("<a href=\"");
            ret.append (nextFile);
            ret.append ("\">Next</a>");
        }
        ret.append (HTML_BOTTOM);
        return ret.toString();
    }
}
