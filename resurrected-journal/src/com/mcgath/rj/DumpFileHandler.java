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
    
    private Comment currentComment;
    private Post currentPost;
    private StringBuffer postBodyBuf;
    private StringBuffer commentBodyBuf;
    private StringBuffer postSubjectBuf;
    private StringBuffer commentSubjectBuf;
    private StringBuffer postDateBuf;
    private StringBuffer commentDateBuf;
    
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
            // TODO deal with the attributes
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
}
