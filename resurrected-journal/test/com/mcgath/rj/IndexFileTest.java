package com.mcgath.rj;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class IndexFileTest {

    @Test
    public void testSkeletalFile() throws Exception {
        File loc = new File ("./temp");
        File oldfile = new File (loc, "index.html");
        oldfile.delete ();
        
        IndexFile indf = new IndexFile (loc, "Title");
        indf.close ();
    }

}
