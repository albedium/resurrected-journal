/** Resurrected Journal
 * 
 * Copyright (c) 2012, Gary McGath
 * All rights reserved.
 * 
 * The developer of this software may be available for enhancements or
 * related development work. See http://www.garymcgath.com for current status.
 * 
 * Licensed under the BSD license:
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this 
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation 
 *  and/or other materials provided with the distribution.
 *  
 *  Neither the name of Gary McGath nor the names of contributors 
 *  may be used to endorse or promote products derived from this software 
 *  without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 *  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mcgath.rj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, String> properties;
    
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
        properties = new HashMap<String, String> ();
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
    
    /** 
     * Add a property. 
     */
    public void addProperty (String name, String value) {
        properties.put(name,  value);
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
        ret.append ("<div class=\"comment\"" +
                "style=\"margin-left: " +
                        Integer.toString (nestLevel * 24) +
                        "pt\">\n");
        ret.append ("<span class=\"cmtcredit\">Posted by " + poster + 
                " at " + date + "</span><br>");
        if (subject != null) {
            ret.append ("<span class=\"cmtsubj\">" + subject + "</span><br>\n");
        }
        ret.append ("<div class=\"cmtbody\">" + body + "</div>\n");
        String ipaddr = properties.get ("poster_ip");
        if (ipaddr != null) {
            ret.append ("<br>&nbsp;<br>IP address: " + ipaddr + "<br>\n");
        }
        if (!replies.isEmpty ()) {
            ret.append ("<p class=\"fill2\"></p>\n");
        }
        for (Comment rep : replies) {
            ret.append (rep.toHTML());
        }
        ret.append ("<p class=\"fill2\"></p>\n");
        ret.append ("</div>");      // Close div class comment
        return ret.toString();
    }
}
