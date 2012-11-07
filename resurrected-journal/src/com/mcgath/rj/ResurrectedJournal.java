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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** Main command line class for RJ */
public class ResurrectedJournal {

    private static Logger logger;
    private final static Level logLevel = Level.WARN;
    

    private String destPath;
    private String srcPath;
    private String title;
    private String stylesheet;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        initLogging ();
        ResurrectedJournal rj = new ResurrectedJournal ();
        try {
            rj. parseArgs (args);
        }
        catch (ParseException e) {
            System.out.println ("Error in command line");
            System.out.println (e.getMessage ());
            return;
        }
        if (rj.destPath == null || rj.srcPath == null || rj.title == null) {
            System.out.println ("Usage: rj -s [sourcepath] -d [destpath] -t [title]");
            return;
        }
        rj.process();
    }

    /** Figure out the arguments. Paths must be in quotes if
     *  they contain breaking characters. The argument indicators are:
     *  
     *  -s followed by source path
     *  -d followed by destination directory path
     *  -t followed by title
     */
    private void parseArgs (String[] args) throws ParseException {
        title = "LiveJournal Archive";    // default title
        PosixParser parser = new PosixParser ();
        Options options = new Options ();
        options.addOption ("s", true, "Source XML file path");
        options.addOption ("d", true, "Destination directory path");
        options.addOption ("t", true, "Title");
        options.addOption ("ss", true, "Stylesheet");
        CommandLine cl = parser.parse (options, args);
        Option[] parsedOpts = cl.getOptions();
        srcPath = null;
        destPath = null;
        title = null;
        for (Option optn : parsedOpts) {
            String opt = optn.getOpt ();
            String val = optn.getValue ();
            if ("s".equals (opt)) {
                srcPath = val;
            }
            else if ("d".equals (opt)) {
                destPath = val;
            }
            else if ("t".equals (opt)) {
                title = val;
            }
            else if ("ss".equals (opt)) {
                stylesheet = val;
            }
        }
    }

    /** Init logging.  */
    private static void initLogging () {
        logger = Logger.getLogger ("com.mcgath.rj");
        BasicConfigurator.configure();
        logger.setLevel(logLevel);
    }

    /** Make it all happen. */
    private void process () {
        IndexFile ixfl = null;
        try {
            File srcf = new File(srcPath);
            if (!srcf.exists()) {
                System.out.println ("File " + srcPath + " does not exist.");
                return;
            }
            else if (srcf.isDirectory()) {
                System.out.println ("File " + srcPath + " is a directory.");
                return;
            }
            System.out.println ("Processing file " + srcPath);
            Stylesheet ssht;
            if (stylesheet != null) {
                File ssf = new File(stylesheet);
                if (!ssf.exists()) {
                    System.out.println ("Stylesheet file " + stylesheet + " does not exist.");
                    return;
                }
                else if (ssf.isDirectory ()) {
                    System.out.println ("File " + stylesheet + " is a directory.");
                    return;
                }
                ssht = new Stylesheet (stylesheet, destPath);
            }
            else {
                ssht = new Stylesheet (destPath);
            }
            ssht.copyToOutput();
            ixfl = new IndexFile
                    (new File (destPath), title);
            XMLDumpFile srcFile = new XMLDumpFile
                    (new File(srcPath), new File(destPath), ixfl);
            srcFile.processFile ();
        }
        catch (Exception e) {
            e.printStackTrace ();
            return;
        }
        finally {
            if (ixfl != null) {
                try {
                    ixfl.close();
                }
                catch (Exception ee) {}
            }
        }
        System.out.println ("Completed conversion to directory " + destPath);
    }
}
