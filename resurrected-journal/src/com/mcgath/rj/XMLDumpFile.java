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
