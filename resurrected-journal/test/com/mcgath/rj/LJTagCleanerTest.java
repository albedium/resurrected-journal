package com.mcgath.rj;

import static org.junit.Assert.*;

import org.junit.Test;

public class LJTagCleanerTest {

    @Test
    public void ljCutTest() {
        final String test1 = "mumble foo <lj-cut text=\"Spoiler Alert\">Hamlet dies.</lj-cut>";
        LJTagCleaner cleaner = new LJTagCleaner(test1);
        String result = cleaner.cleanText();
        assertEquals ("mumble foo Hamlet dies.", result);
    }

    @Test
    public void ljCutTwoCutTest() {
        final String test1 = "mumble foo <lj-cut text=\"Spoiler Alert\">Hamlet dies.</lj-cut>" +
                "<lj-cut text='In single quotes'> Horatio <i>survives</i>.</lj-cut> Sorry.";
        LJTagCleaner cleaner = new LJTagCleaner(test1);
        String result = cleaner.cleanText();
        assertEquals ("mumble foo Hamlet dies." +
                " Horatio <i>survives</i>. Sorry.", result);
    }

    @Test
    public void ljUserTest () {
        final String test1 = "foo <lj user=\"frank\"> bar";
        LJTagCleaner cleaner = new LJTagCleaner (test1);
        String result = cleaner.cleanText();
        assertEquals ("foo <span class=\"uname\">frank</span> bar", result);
    }
}
