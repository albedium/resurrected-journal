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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *  Handles the copying of the CSS stylesheet to the output directory. 
 */
public class Stylesheet {

    public final static String STYLESHEET_NAME = "style.css";

    private final static int BUFFER_SIZE = 32768;
    
    private File srcFile;
    private File destDir;
    
    /** 
     *  Constructor for explicit file to copy.
     *  The file will always be named "style.css" in the destination
     *  directory.
     *  
     *  @param src   The source stylesheet file path
     *  @param dest  The destination directory path
     */
    public Stylesheet (String src, String dest) {
        srcFile = new File(src);
        destDir = new File (dest);
    }
    
    /**
     *  Constructor for copying default stylesheet.
     *  It will be copied to "style.css" in the destination directory.
     *  
     *  @param dest   The destination directory path
     */
    public Stylesheet (String dest) {
        srcFile = null;
        destDir = new File(dest);
    }
    
    /** 
     *  Copy the designated stylesheet to the destination directory. 
     */
    public void copyToOutput () throws IOException {
        File destFile = new File (destDir, STYLESHEET_NAME);
        if (srcFile == null) {
            copyFromResource (destFile);
        }
        else {
            copyFromFile (destFile);
        }
    }
    
    private void copyFromResource (File destFile) throws IOException {
        InputStream is = this.getClass().getResourceAsStream ("style.css");
        copy (is, destFile);
    }

    private void copyFromFile (File destFile) throws IOException {
        FileInputStream fis = new FileInputStream (srcFile);
        copy (fis, destFile);
    }

    
    private void copy (InputStream instrm, File destFile) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        FileOutputStream fos = new FileOutputStream (destFile);
        try {
            for (;;) {
                int n = instrm.read (buf);
                if (n <= 0) {
                    break;
                }
                else {
                    fos.write (buf, 0, n);
                }
            }
        }
        finally {
            instrm.close ();
            fos.close ();
        }
    }

}
