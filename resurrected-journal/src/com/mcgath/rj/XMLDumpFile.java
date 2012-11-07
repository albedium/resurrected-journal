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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** This class controls the writing of the XML file to multiple output files */
public class XMLDumpFile {

    private final static Logger logger = Logger.getLogger("com.mcgath.rj");
    private final static String POST_NAME = "post";
    
    private SAXParser parser;
    private InputSource inputSrc;
    //private FileWriter writer;
    private File destDirectory;
    private IndexFile indexFile;
    
    int fileSequenceNo = 0;
    
    /** Constructor with file */
    public XMLDumpFile (File fil, File destDir, IndexFile ixfl) 
            throws IOException, SAXException, ParserConfigurationException {
        logger.debug ("Opening XMLDumpFile at " + fil.getAbsolutePath());
        destDirectory = destDir;
        indexFile = ixfl;
        if (!destDir.exists ()) {
            System.out.println ("Creating directory " + destDir.getAbsolutePath ());
            destDir.mkdirs();
            if (!destDir.exists()) {
                throw new IOException ("Could not create directory");
            }
        }
        else if (!destDir.isDirectory()) {
            throw new IOException ("Destination path is not a directory");
        }
        try {
            parser = createSAXParser();
            FileInputStream fstrm = new FileInputStream (fil);
            inputSrc = new InputSource (fstrm);
        }
        catch (SAXException e) {
            logger.error ("Can't set up SAX parser: " + 
                    e.getClass().getName() + ": " +
                    e.getMessage ());   
            throw e;
        }
        catch (IOException e) {
            logger.error ("Exception in SAXParser: " + 
                    e.getClass().getName() + ": " +
                    e.getMessage ());   
            throw e;
        }
        catch (ParserConfigurationException e) {
            logger.error ("ParserConfigurationException: " + 
                    e.getMessage ());   
            throw e;
        }
    }
    
    /** Constructor with XML string, to facilitate testing */
    public XMLDumpFile (String xml) 
            throws SAXException, IOException, ParserConfigurationException {
        StringReader rdr = new StringReader (xml);
        try {
            parser = createSAXParser();
            inputSrc = new InputSource (rdr);
        }
        catch (SAXException e) {
            logger.error ("Can't set up SAX parser: " + 
                    e.getClass().getName() + ": " +
                    e.getMessage ());   
            throw e;
        }
        catch (IOException e) {
            logger.error ("Exception in SAXParser: " + 
                    e.getClass().getName() + ": " +
                    e.getMessage ());   
            throw e;
        }
        catch (ParserConfigurationException e) {
            logger.error ("ParserConfigurationException: " + 
                    e.getMessage ());   
            throw e;
        }
    }
    
    public void processFile () throws SAXException, IOException {
        parser.parse (inputSrc, new DumpFileHandler (this));
    }

    /** Write out a post to the file */
    public void writePost(Post p) throws IOException {
        if (fileSequenceNo > 0) {
            p.setPreviousFile (POST_NAME + (fileSequenceNo - 1) + ".html");
        }
        p.setNextFile (POST_NAME + (fileSequenceNo + 1) + ".html");
        // TODO this will result in a broken link at the end unless we
        // do some kind of lookahead.
        if (destDirectory != null) {
            String postFileName = POST_NAME + fileSequenceNo + ".html";
            File postFile = new File (destDirectory, postFileName);
            FileWriter postFileWriter = new FileWriter (postFile);
            postFileWriter.write(p.toHTML());
            postFileWriter.flush ();
            postFileWriter.close ();
            String subj = p.getSubject ();
            if (subj == null) {
                subj = "(Post " + fileSequenceNo;
            }
            indexFile.writeEntry(subj, postFileName);
            fileSequenceNo++;
        }
    }
    
    
    /** Create the SAX parser. */
    private SAXParser createSAXParser () 
            throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory fac = SAXParserFactory.newInstance();
        SAXParser parser = fac.newSAXParser ();
        return parser;
    }

}
