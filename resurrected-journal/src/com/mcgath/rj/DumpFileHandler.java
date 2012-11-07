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

import java.io.IOException;
import java.util.List;

import javax.xml.stream.events.Attribute;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Handler for XML parsing of dump file */
public class DumpFileHandler extends DefaultHandler {

    private static Logger logger = Logger.getLogger("com.mcgath.rj");

    private XMLDumpFile xmlDumpFile;
    
    private boolean inEvents = false;
    private boolean inPost = false;
    private boolean inPostBody = false;
    private int commentsNestLevel = 0;
    private boolean inProps = false;
    private boolean inProp = false;
    private boolean inDate = false;
    private boolean inCommentBody = false;
    private boolean inPostSubject = false;
    private boolean inCommentSubject = false;
    private boolean inPostDate = false;
    private boolean inCommentDate = false;
    private boolean inPostProps = false;
    private boolean inPostProp = false;
    private boolean inCommentProps = false;
    private boolean inCommentProp = false;
    
    private Comment currentComment;
    private Post currentPost;
    private String commentPropName;
    
    private StringBuffer postBodyBuf;
    private StringBuffer commentBodyBuf;
    private StringBuffer postSubjectBuf;
    private StringBuffer commentSubjectBuf;
    private StringBuffer postDateBuf;
    private StringBuffer commentDateBuf;
    private StringBuffer commentPropBuf;
    
    public DumpFileHandler (XMLDumpFile f) {
        xmlDumpFile = f;
    }
    
    
    @Override
    public void startElement
          (String uri, String localName, String qName, Attributes attributes) {
        logger.info ("startElement, qName = " + qName);
        if ("events".equals (qName)) {
            startEventsElement (attributes);
        }
        else if ("event".equals (qName)) {
            startEventElement (attributes);
        }
        else if ("comment".equals (qName)) {
            startCommentElement (attributes);
        }
        else if ("body".equals (qName)) {
            startBodyElement (attributes);
        }
        else if ("subject".equals (qName)) {
            startSubjectElement (attributes);
        }
        else if ("date".equals (qName)) {
            startDateElement (attributes);
        }
        else if ("props".equals (qName)) {
            startPropsElement (attributes);
        }
        else if ("prop".equals (qName)) {
            startPropElement (attributes);
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) 
              throws SAXException {
        if ("events".equals (qName)) {
            endEventsElement ();
        }
        else if ("event".equals (qName)) {
            try {
                endEventElement ();
            }
            catch (IOException e) {
                throw new SAXException ("Parsing aborted due to I/O problem", e);
            }
        }
        else if ("comment".equals (qName)) {
            endCommentElement ();
        }
        else if ("body".equals (qName)) {
            endBodyElement ();
        }
        else if ("subject".equals (qName)) {
            endSubjectElement ();
        }
        else if ("date".equals (qName)) {
            endDateElement ();
        }
        else if ("props".equals (qName)) {
            endPropsElement ();
        }
        else if ("prop".equals (qName)) {
            endPropElement ();
        }
    }
    
    @Override
    public void characters(char[] ch, int start, int length) {
        if (inCommentBody) {
            commentBodyBuf.append(ch, start, length);
        }
        else if (inPostBody) {
            postBodyBuf.append(ch, start, length);
        }
        else if (inPostSubject) {
            postSubjectBuf.append(ch, start, length);
        }
        else if (inCommentSubject) {
            commentSubjectBuf.append (ch, start, length);
        }
        else if (inPostDate) {
            postDateBuf.append (ch, start, length);
        }
        else if (inCommentDate) {
            commentDateBuf.append (ch, start, length);
        }
        else if (inCommentProp) {
            commentPropBuf.append (ch, start, length);
        }
    }
    
    
    private void startEventsElement (Attributes atts) {
        inEvents = true;
    }
    
    private void endEventsElement () {
        inEvents = false;
    }
    
    /* The element name "event" is used for both a whole post
     * and the text body of the post. We have to keep track of where
     * we are to distinguish between the two. */
    private void startEventElement (Attributes atts) {
        if (!inEvents) {
            logger.error ("Misplaced event element");
            return;    // element out of place
        }
        if (!inPost) {
            currentPost = new Post ();
            String autoFormat = atts.getValue("allowmask");
            currentPost.setAutoFormat ("1".equals (autoFormat));
            inPost = true;
        }
        else if (!inPostBody) {
            inPostBody = true;
            postBodyBuf = new StringBuffer ();
        }
        else {
            logger.error ("Invalid state for start of event element");
        }
    }
    
    /* This can be either the end of a post or the end of a post body */
    private void endEventElement ()  throws IOException {
        if (inPostBody) {
            currentPost.setBody (postBodyBuf.toString());
            inPostBody = false;
            //xmlDumpFile.writePost(currentPost);
        }
        else if (inPost) {
            xmlDumpFile.writePost (currentPost);
            inPost = false;
        }
        else {
            logger.error ("Invalid state for end of event element");
        }
    }
    
    /** Comments are nested. */
    private void startCommentElement (Attributes atts) {
        if (!inPost) {
            logger.error ("Misplaced comment element");
            return;
        }
        ++commentsNestLevel;
        if (commentsNestLevel > 1 && currentComment == null) {
            logger.error ("In nested comments, but no currentComment");
        }
        Comment newComment;
        if (commentsNestLevel > 1) {
            newComment = new Comment (currentComment);
        }
        else {
            newComment = new Comment();
            currentPost.addComment (newComment);
        }
        String autoFormat = atts.getValue("allowmask");
        newComment.setAutoFormat ("1".equals (autoFormat));

        String poster = atts.getValue ("poster");
        String posterid = atts.getValue ("posterid");
        if (poster == null) {
            poster = "(User ID " + posterid + ")";
        }
        newComment.setPoster (poster);
        currentComment = newComment;
    }
    
    private void endCommentElement () {
        if (commentsNestLevel <= 0 || currentComment == null) {
            logger.error ("Comment nesting error");
            return;
        }
        --commentsNestLevel;
        Comment theComment = currentComment;
        currentComment = theComment.getParent ();
    }
    
    /* The "body" element belongs to a comment */
    private void startBodyElement (Attributes atts) {
        if (commentsNestLevel <= 0) {
            return;
        }
        commentBodyBuf = new StringBuffer ();
        inCommentBody = true;
    }
    
    private void endBodyElement () {
        if (!inCommentBody) {
            return;
        }
        currentComment.setBody(commentBodyBuf.toString());
        inCommentBody = false;
    }
    
    /* Subject may belong to either a post or a comment. */
    private void startSubjectElement (Attributes atts) {
        if (inPost) {
            inPostSubject = true;
            postSubjectBuf = new StringBuffer ();
        }
        else if (commentsNestLevel > 0) {
            inCommentSubject = true;
            commentSubjectBuf = new StringBuffer ();
        }
    }
    
    private void endSubjectElement () {
        if (inPostSubject) {
            currentPost.setSubject (postSubjectBuf.toString ());
            inPostSubject = false;
        }
        else if (inCommentSubject) {
            currentComment.setSubject (commentSubjectBuf.toString ());
            inCommentSubject = false;
        }
    }
    
    /* Date may belong to either a post or a comment. */
    private void startDateElement (Attributes atts) {
        if (inPost && commentsNestLevel == 0) {
            inPostDate = true;
            postDateBuf = new StringBuffer ();
        }
        else if (commentsNestLevel > 0) {
            inCommentDate = true;
            commentDateBuf = new StringBuffer ();
        }
    }
    
    private void endDateElement () {
        if (inPostDate) {
            currentPost.setDate (postDateBuf.toString ());
            inPostDate = false;
        }
        else if (inCommentDate) {
            currentComment.setDate (commentDateBuf.toString ());
            inCommentDate = false;
        }
    }
    
    /* Comments and posts can both have props elements. */
    private void startPropsElement (Attributes atts) {
        if (inPost && commentsNestLevel == 0) {
            inPostProps = true;
        }
        else if (commentsNestLevel > 0) {
            inCommentProps = true;
        }
    }
    
    private void endPropsElement () {
        inPostProps = false;
        inCommentProps = false;
        // Let the devil sort them out...
    }
    
    /* A post prop element uses the name and value attributes, but a comment prop
     * element uses the element content. Go figure. */
    private void startPropElement (Attributes atts) {
        String name = atts.getValue("name");
        String value = atts.getValue ("value");
        if (inPostProps) {
            inPostProp = true;
            if (name != null && value != null) {
                currentPost.addProperty(name, value);
            }
        }
        else if (inCommentProps) {
            inCommentProp = true;
            commentPropBuf = new StringBuffer ();
            commentPropName = name;
        }
    }
    
    private void endPropElement () {
        if (inCommentProp) {
            currentComment.addProperty(commentPropName, commentPropBuf.toString());
        }
        inPostProp = false;
        inCommentProp = false;
    }
}
