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
import java.io.FileWriter;
import java.io.IOException;

/** This class is responsible for the creation of the index.html file
 *  that has links to all the entry files. */
public class IndexFile {

    private FileWriter writer;
    
    private final static String fileHead = "<html>\n" +
       "<head>\n<title>%1</title>" +
       "<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\">\n" + 
       "</head>\n" +
       "<body>\n\n" +
       "<h1>%1</h1>" +
       "<ul>";
    
    private final static String fileTail = "</ul>\n" +
        "</body> </html>";
    
    /** Constructor. Sets up a writer and outputs the header.
     * 
     *  @param  parent   The parent directory
     */
    public IndexFile (File parent, String title) throws IOException {
        File ifil = new File (parent, "index.html");
        writer = new FileWriter (ifil);
        String fh1 = fileHead.replace ("%1", title);
        writer.write (fh1);
    }
    
    /** Add an entry to the index file
     *  
     *  @param subject    Subject text, appears as the link name
     *  @param fileName   Name of post file, becomes the link target
     *  */
    public void writeEntry (String subject, String fileName) throws IOException {
        writer.write ("<li>" +
            "<a href=\"" +
            fileName +
            "\">" +
            subject +
            "</a></li>\n");
    }
    
    /** Write the closing HTML tags and close the file. */
    public void close () throws IOException {
        writer.write (fileTail);
        writer.flush ();
        writer.close ();
    }
}
