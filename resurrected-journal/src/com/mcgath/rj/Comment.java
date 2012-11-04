package com.mcgath.rj;

import java.util.ArrayList;
import java.util.List;

/** A single LJ comment. */
public class Comment {

    private List<Comment> replies;
    private Comment parentComment;
    private String body;
    private String subject;
    private String poster;
    private String date;
    private String editTime;
    private String posterIP;
    private boolean autoFormat;
    
    private int nestLevel;
    
    /** Constructor for top-level comment */
    public Comment () {
        init();
    }
    
    /** Constructor for reply comment. Links the comment bidirectionally. */
    public Comment (Comment parent) {
        init ();
        parentComment = parent;
        if (parent != null) {
            parent.replies.add (this);
            nestLevel = parent.nestLevel + 1;
        }
    }

    private void init () {
        autoFormat = true;    // default
        replies = new ArrayList<Comment> ();
        nestLevel = 0;
    }
    
    
    /** Get the nesting level of the comment */
    public int getNestLevel () {
        return nestLevel;
    }
    
    /** Set the text body of the comment */
    public void setBody (String s) {
        if (autoFormat) {
            body = s.replace("\n", "<p>\n");
        }
        else {
            body = s;
        }
    }
    
    /** Set the subject of the comment */
    public void setSubject (String s) {
        subject = s;
    }
    
    /** Set the poster of the comment */
    public void setPoster (String s) {
        poster = s;
    }
    
    /** Set the auto-format flag */
    public void setAutoFormat (boolean b) {
        autoFormat = b;
    }
    
    /** Set the date string */
    public void setDate (String s) {
        date = s;
    }
    
    /** Get the comment's parent. This will be null if it isn't a reply
     *  to another comment. */
    public Comment getParent () {
        return parentComment;
    }
    
    /** Get the replies to the comment. */
    public List<Comment> getReplies () {
        return replies;
    }
    
    /** Turn into an HTML div */
    public String toHTML () {
        // TODO barely more than a stub
        // TODO should use CSS rather than header tags
        StringBuffer ret = new StringBuffer ( );
        ret.append ("<h2>Posted by " + poster + " at " + date + "</h2>");
        ret.append (body);
        if (subject != null) {
            ret.append ("<h2>" + subject + "</h2>\n");
        }
        for (Comment rep : replies) {
            ret.append (rep.toHTML());
        }
        String divTag = "<div style=\"margin-left: " +
             Integer.toString (nestLevel * 24) +
             "pt\">";
        return divTag + "<p>\n" + ret.toString() + "\n</p></div>\n";
    }
}
