/* Copyright (C) 2006-2016 Patrick G. Durand
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.txt
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 */
package bzh.plealog.bioinfo.bft;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Starter of the application.
 * 
 * @author Patrick G. Durand
 */
public class Main {
	private static final String HELP_HEADER_NAME = "java -jar blast-filter-tool-X.Y.jar <args>";

	/**
	 * Prepare the command-line options.
	 */
	private static Options setupCmdLineOptions(){
		Options options = new Options();
		Option  opt;

		// no argument options
		// help
		options.addOption("h", "print this message");
		// verbose mode
		options.addOption("n", "verbose mode off; default in on");
		// editor tool (UI)
		opt = new Option("e", "launch the Filter Editor");
		options.addOption(opt);

		// options with argument(s)
		// input file
		opt = new Option("i", true, "Single BLAST result file, legacy XML formated [mandatory]. Exclusive with '-d'.");
		options.addOption(opt);
		// output file
		opt = new Option("o", true, "result file");
		options.addOption(opt);
		// filter file
		opt = new Option("f", true, "filter file [mandatory]");
		options.addOption(opt);
		// directory
		opt = new Option("d", true, "Directory with BLAST result files, legacy XML formated [mandatory]. Exclusive with '-i'.");
		options.addOption(opt);
		// pattern to scan in directory
		opt = new Option("p", true, "Pattern to locate BLAST files in directory. Default is '*.*'. Use with '-d'.");
		options.addOption(opt);
		return options;
	}
	/**
	 * Start application.
	 */
	public static void main(String[] args) {
		// create the command-line options
		Options options = setupCmdLineOptions();
		// create the parser
		CommandLineParser parser = new DefaultParser();
		// get software properties
		Properties props = Help.getVersionProperties();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse( options, args );
			if (line.hasOption("h")){
				// help requested
				Help.dumpHeader(props);
			  System.out.println("");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(HELP_HEADER_NAME, options);
				Help.dumpUsages(props);
			}
			else if (line.hasOption("e")){
				// editor requested
				BlastFilterTool.startEditor(args);
			}
			else{
				if(!line.hasOption("n")){
					Help.dumpHeader(props);
				}
				// filter a data file requested
				BlastFilterTool.applyFilter(line);
			}
		}
		catch( ParseException exp ) {
			System.err.println( "Command-line Parsing failed.  Reason: " + exp.getMessage() );
		}
	}

}
