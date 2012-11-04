package com.mcgath.rj;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testComments() {
        Comment baseComment = new Comment ();
        baseComment.setBody ("LOL!");
        Comment response = new Comment (baseComment);
        response.setBody("+1");
        Comment secondResponse = new Comment (baseComment);
        secondResponse.setBody("This.");
        Comment flame = new Comment (secondResponse);
        flame.setBody ("Idiot.");
        assertEquals (flame.getNestLevel(), 2);
        List<Comment> replies = baseComment.getReplies ();
        assertEquals (replies.size(), 2);
        assertEquals (response, replies.get(0));
        assertEquals (secondResponse, replies.get(1));
        
        replies = secondResponse.getReplies ();
        assertEquals (flame, replies.get(0));
    }
    
    @Test
    public void testCommentsWithPost () {
        Post post = new Post ();
        Comment firstComment = new Comment ();
        post.addComment(firstComment);
        Comment secondComment = new Comment ();
        post.addComment(secondComment);
        List<Comment> comments = post.getComments ();
        assertEquals (comments.size(), 2);
        assertEquals (firstComment, comments.get(0));
        assertEquals (secondComment, comments.get(1));
    }
    
    @Test
    public void testHTML () {
        Comment comment = new Comment ();
        comment.setBody ("Yes!");
        Comment reply1 = new Comment (comment);
        reply1.setBody ("No!");
        Comment reply2 = new Comment (comment);
        reply2.setBody ("Maybe!");
        Comment reply3 = new Comment (reply2);
        reply3.setBody ("Make up your mind!");
        String html = comment.toHTML();
        assertTrue (html.indexOf ("Yes") > 0);
        assertTrue (html.indexOf ("No") > 0);
        assertTrue (html.indexOf ("Maybe") > 0);
        assertTrue (html.indexOf ("Make up") > 0);
    }

}
