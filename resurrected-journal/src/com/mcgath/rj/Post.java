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

/** A LiveJournal post (or "event") */
public class Post {

    private class Mood {
        private String text;
        private Integer index;
        
        /** Self-inserting constructor */
        public Mood (String text, int index) {
            this.text = text;
            this.index = index;
            moodMap.put(this.index, this.text);
        }
        
        
    }
    
    
    private final static String HTML_TOP = "<html><head>\n" +
                 "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\">\n" + 
                 "<title>%1</title>\n" +
                 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                 "</head>\n" +
                 "<body>\n";
    
    private final static String HTML_BOTTOM = "</body></html>";
    private static Map<Integer, String> moodMap;
    
    {
        moodMap = new HashMap<Integer, String> ();
        mapMood(32, "touched");
        mapMood(127, "distressed");
        mapMood(90, "accomplished");
        mapMood(118, "predatory");
        mapMood(71, "pessimistic");
        mapMood(102, "nerdy");
        mapMood(18, "hungry");
        mapMood(125, "cheerful");
        mapMood(16, "high");
        mapMood(44, "amused");
        mapMood(55, "disappointed");
        mapMood(27, "sore");
        mapMood(84, "cold");
        mapMood(57, "mellow");
        mapMood(95, "grumpy");
        mapMood(20, "irate");
        mapMood(109, "pleased");
        mapMood(89, "productive");
        mapMood(31, "tired");
        mapMood(35, "ditzy");
        mapMood(11, "energetic");
        mapMood(78, "exanimate");
        mapMood(93, "full");
        mapMood(106, "crazy");
        mapMood(29, "thirsty");
        mapMood(65, "indifferent");
        mapMood(114, "apathetic");
        mapMood(58, "peaceful");
        mapMood(15, "happy");
        mapMood(81, "sympathetic");
        mapMood(60, "nostalgic");
        mapMood(73, "pensive");
        mapMood(101, "contemplative");
        mapMood(86, "loved");
        mapMood(76, "listless");
        mapMood(62, "rejuvenated");
        mapMood(67, "flirty");
        mapMood(129, "crushed");
        mapMood(2, "angry");
        mapMood(17, "horny");
        mapMood(82, "sick");
        mapMood(110, "bitchy");
        mapMood(14, "exhausted");
        mapMood(69, "refreshed");
        mapMood(112, "irritated");
        mapMood(49, "sleepy");
        mapMood(24, "pissed off");
        mapMood(124, "numb");
        mapMood(104, "cynical");
        mapMood(131, "thankful");
        mapMood(121, "surprised");
        mapMood(79, "embarrassed");
        mapMood(23, "moody");
        mapMood(96, "weird");
        mapMood(126, "good");
        mapMood(47, "frustrated");
        mapMood(8, "cranky");
        mapMood(98, "ecstatic");
        mapMood(37, "morose");
        mapMood(117, "naughty");
        mapMood(43, "hopeful");
        mapMood(5, "bored");
        mapMood(33, "lazy");
        mapMood(63, "complacent");
        mapMood(21, "jubilant");
        mapMood(7, "crappy");
        mapMood(80, "envious");
        mapMood(26, "satisfied");
        mapMood(119, "dirty");
        mapMood(99, "chipper");
        mapMood(72, "giggly");
        mapMood(74, "uncomfortable");
        mapMood(61, "okay");
        mapMood(108, "artistic");
        mapMood(115, "dorky");
        mapMood(92, "blah");
        mapMood(103, "geeky");
        mapMood(10, "discontent");
        mapMood(113, "blank");
        mapMood(91, "busy");
        mapMood(107, "creative");
        mapMood(48, "indescribable");
        mapMood(87, "awake");
        mapMood(77, "recumbent");
        mapMood(133, "jealous");
        mapMood(123, "rejected");
        mapMood(39, "melancholy");
        mapMood(64, "content");
        mapMood(97, "nauseated");
        mapMood(12, "enraged");
        mapMood(41, "excited");
        mapMood(52, "hyper");
        mapMood(56, "curious");
        mapMood(45, "determined");
        mapMood(66, "silly");
        mapMood(19, "infuriated");
        mapMood(54, "restless");
        mapMood(70, "optimistic");
        mapMood(68, "calm");
        mapMood(1, "aggravated");
        mapMood(88, "working");
        mapMood(116, "impressed");
        mapMood(30, "thoughtful");
        mapMood(100, "rushed");
        mapMood(128, "intimidated");
        mapMood(25, "sad");
        mapMood(120, "giddy");
        mapMood(28, "stressed");
        mapMood(40, "drained");
        mapMood(83, "hot");
        mapMood(75, "lethargic");
        mapMood(134, "nervous");
        mapMood(59, "bouncy");
        mapMood(130, "devious");
        mapMood(53, "relaxed");
        mapMood(122, "shocked");
        mapMood(22, "lonely");
        mapMood(42, "relieved");
        mapMood(46, "scared");
        mapMood(13, "enthralled");
        mapMood(105, "quixotic");
        mapMood(6, "confused");
        mapMood(85, "worried");
        mapMood(3, "annoyed");
        mapMood(36, "mischievous");
        mapMood(111, "guilty");
        mapMood(51, "groggy");
        mapMood(9, "depressed");
        mapMood(38, "gloomy");
        mapMood(4, "anxious");
        mapMood(34, "drunk");
        mapMood(132, "grateful");
                    
    }
    
    private List<Comment> comments;
    private String body;
    private String date;
    private String subject;
    private boolean autoFormat;
    private Map<String, String> properties;
    
    private String previousFile;
    private String nextFile;
    
    public Post () {
        comments = new ArrayList<Comment> ();
        body = "";
        autoFormat = true;  // assume autoformat as default
        subject = null;
        properties = new HashMap<String, String> ();
    }
    
    /** Static function for setting up mood map */
    private static void mapMood(Integer idx, String text) {
        moodMap.put(idx, text);
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
    
    /** 
     * Add a property. 
     */
    public void addProperty (String name, String value) {
        properties.put(name,  value);
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
        String subj = (subject == null) ? "(No subject)" : subject;
        StringBuffer ret = new StringBuffer (HTML_TOP.replace("%1", subj));
        ret.append("<span class=\"postsubj\">" + subj + "</span><br>\n");
        ret.append ("<span class=\"postdate\">Date: " + date + "</span><br>\n");
        ret.append("<div class=\"postbody\">" + body + "<br></div>\n");
        
        boolean propBreak = false;    // Break just once if there are any properties
        String currentMusic = properties.get ("current_music");
        if (currentMusic != null) {
            if (!propBreak) {
                ret.append ("<br>&nbsp;<br>");
                propBreak = true;
            }
            ret.append ("<span class=\"postprop\">Music: " +
                 currentMusic + "</span><br>\n");
        }
        
        String security = "public";  // default
        String secProp = properties.get ("security");
        if ("private".equals (secProp)) {
            security = "private";
        }
        if ("usemask".equals (secProp)) {
            security = "limited";   // That's as much as I can figure out so far
        }
        ret.append ("<br>&nbsp;<br>\n");
        ret.append ("<span class=\"postprop\"Security: " + security +
                      "</span><br>\n");

        // Mood may be from either current_moodid or current_mood
        String moodID = properties.get ("current_moodid");
        String mood = null;
        if (moodID != null) {
            try {
                int moodidx = Integer.parseInt (moodID);
                mood = moodMap.get (moodidx);
            }
            catch (Exception e) {}
            
        }
        if (mood == null) {
            mood = properties.get ("current_mood");
        }
        if (mood != null) {
            ret.append ("<span class=\"postprop\">Mood: " +
                    mood + "</span><br>\n");        
        }
        
        String tags = properties.get("taglist");
        if (tags != null) {
            ret.append ("<span class=\"postprop\">Tags: " +
                    tags + "</span><br>\n");        
        }
        if (!comments.isEmpty()) {
            ret.append ("<div class=\"comments\"><p class=\"fill1\">&nbsp;</p>");
            ret.append ("<span class=\"commentslabel\">Comments:</span><br>\n");
            for (Comment c : comments) {
                ret.append (c.toHTML());
            }
            ret.append ("</div>");
        }
        ret.append ("<hr>\n");
        ret.append ("<table class=\"footer\"><tr><td>\n");
        if (previousFile != null) {
            ret.append ("<td>");
            ret.append ("<a href=\"");
            ret.append (previousFile);
            ret.append ("\">Previous</a></td>\n");
        }
        else {
            ret.append ("<td>&nbsp;</td>\n");
        }
        if (nextFile != null) {
            ret.append ("<td>");
            ret.append ("<a href=\"");
            ret.append (nextFile);
            ret.append ("\">Next</a></td>\n");
        }
        else {
            ret.append ("<td>&nbsp;</td>\n");
        }
        ret.append (HTML_BOTTOM);
        return ret.toString();
    }
}
