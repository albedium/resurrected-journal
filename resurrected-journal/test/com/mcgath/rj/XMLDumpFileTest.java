package com.mcgath.rj;

import static org.junit.Assert.*;

import org.junit.Test;

public class XMLDumpFileTest {

    private String minimalXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<livejournal server='www.livejournal.com' username='madfilkentist'>\n" +
            "<events>\n" +
            "</events>\n" +
            "</livejournal>";

    
    @Test
    public void testSetUpParser () throws Exception {
        XMLDumpFile df = new XMLDumpFile (minimalXML);
        df.processFile ();
    }

}
