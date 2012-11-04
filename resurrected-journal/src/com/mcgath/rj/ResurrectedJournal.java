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
            System.out.println ("Processing file " + srcPath);
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
