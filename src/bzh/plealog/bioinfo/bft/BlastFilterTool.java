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

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import bzh.plealog.bioinfo.api.core.config.CoreSystemConfigurator;
import bzh.plealog.bioinfo.api.data.searchresult.SRHit;
import bzh.plealog.bioinfo.api.data.searchresult.SRIteration;
import bzh.plealog.bioinfo.api.data.searchresult.SROutput;
import bzh.plealog.bioinfo.api.data.searchresult.io.SRLoader;
import bzh.plealog.bioinfo.api.data.searchresult.io.SRLoaderException;
import bzh.plealog.bioinfo.api.data.searchresult.io.SRWriter;
import bzh.plealog.bioinfo.api.filter.BFilter;
import bzh.plealog.bioinfo.api.filter.config.FilterSystemConfigurator;
import bzh.plealog.bioinfo.io.searchresult.SerializerSystemFactory;
import bzh.plealog.bioinfo.ui.modules.filter.FilterManagerUI;

/**
 * This class contains utilities code for the command-line tool.
 *
 * @author Patrick G. Durand
 */
public class BlastFilterTool {
	// setup an NCBI Blast Loader (XML)
	private static SRLoader ncbiBlastLoader = SerializerSystemFactory.getLoaderInstance(SerializerSystemFactory.NCBI_LOADER);

	// setup an NCBI Blast Writer (XML)
	private static SRWriter ncbiBlastWriter = SerializerSystemFactory.getWriterInstance(SerializerSystemFactory.NCBI_WRITER);

	static {
		// Log4J is required by Castor XML framework
		BasicConfigurator.configure();
		//the following is done to avoid debug traces from Castor XML Framework
		Logger logger = Logger.getRootLogger();
		logger.setLevel(Level.INFO);

		// Initialize Plealog Bioinformatics libraries
		CoreSystemConfigurator.initializeSystem();
		FilterSystemConfigurator.initializeSystem();
	}
	/**
	 * Start Graphical Filter Editor.
	 */
	public static void startEditor(String[] args){
		FilterManagerUI.startUI(args);
	}
	private static String getResultContent(SROutput bo){
		StringBuffer buf;
		SRIteration  iter;
		SRHit        hit;
		int          i, j, nhits, nhsps, totHsps=0, totHits=0, totIter=0;

		for (i=0;i<bo.countIteration();i++){
			totIter++;
			iter = bo.getIteration(i);
			nhits = iter.countHit();
			totHits+=nhits;
			for(j=0;j<nhits;j++){
				hit = iter.getHit(j);
				nhsps = hit.countHsp();
				totHsps+=nhsps;
			}
		}
		buf = new StringBuffer();
		buf.append(totIter);
		buf.append(" iteration");
		if (totIter>1){
			buf.append("s");
		}
		buf.append(" ; ");
		buf.append(totHits);
		buf.append(" hit");
		if (totHits>1){
			buf.append("s");
		}
		buf.append(" ; ");
		buf.append(totHsps);
		buf.append(" HSP");
		if (totHits>1){
			buf.append("s");
		}
		return buf.toString();
	}
	
	/**
	 * Dump the filter.
	 */
	public static void dumpFilter(BFilter filter){
		System.out.println("    filter is: "+filter.toString());
	}
	/**
	 * Read a filter from a file.
	 */
	public static BFilter readFilter(String filterFile, boolean verbose){
		if (verbose){
			System.out.println("  read filter: "+filterFile);
		}
		return FilterSystemConfigurator.getSerializer().load(
				FilterSystemConfigurator.getFilterableModel(), 
				new File(filterFile));
	}
	/**
	 * Apply a filter using extended parameters. This method can be called several times using same
	 * filter parameter. However, be careful: since a Filter is not thread safe do not use a same 
	 * filter in multiple threads (BLAST parallel filtering, for instance). 
	 */
	public static void applyFilter(String inFile, String outFile, BFilter filter, boolean verbose){
		// read NCBI XML blast file
		if (verbose){
			System.out.println("  read input file: "+inFile);
		}
		SROutput bo;
		try {
			bo = ncbiBlastLoader.load(new File(inFile));
		} catch (SRLoaderException e) {
			System.err.println("  ERROR: unable to read filter file.");
			System.err.println("  "+e.toString());
			return;
		}

		if (bo==null || bo.isEmpty()){
			System.err.println("    empty");
			return;
		}
		if (verbose){
			System.out.println("    content: "+getResultContent(bo));
		}

		// apply the filter on the data, and save result if any
		SROutput boDest = filter.execute(bo);
		if (verbose){
			System.out.println("  filtering done");
		}
		if (boDest!=null && boDest.isEmpty()==false){
			if (verbose){
				System.out.println("    content: "+getResultContent(boDest));
				System.out.println("    writing file: "+outFile);
			}
			ncbiBlastWriter.write(new File(outFile), boDest);
		}
		else{
			if (verbose){
				System.out.println("    no result; no output file created");
			}
		}
	}
	/**
	 * Apply a filter on a directory of results.
	 */
	public static void applyFilter (String inDirectory, String argPattern, String outFile_suffix, BFilter filter, boolean verbose ) {
		final String pattern = argPattern.replace(".","\\.").replace("*",".*");
		if (verbose){
			System.out.println("  Directory to scan: "+inDirectory);
			System.out.println("    with pattern: "+argPattern);
		}
		//For now, this is a sequential processing of files
		//Of course, it could be ran in //. Not done, yet.
		//And in that case: DO NOT share filter between threads since a filter is not thread safe.
		for( File f : new File(inDirectory).listFiles( new FilenameFilter(){
			public boolean accept( File dir, String name ) { 
				return name.matches( pattern );
			}
		})){
			applyFilter(f.getAbsolutePath(), f.getAbsolutePath()+outFile_suffix, filter, verbose);
		}
	}
	/**
	 * Apply a filter using a command line.
	 */
	public static void applyFilter(CommandLine line){
		String  inFile="", outFile="_filtered", filterFile, pattern="*.*", directory="";
		boolean verbose, scanSingleFile=true;

		verbose = !line.hasOption("n");
		// control mandatory arguments
		if (line.hasOption("i")==false && line.hasOption("d")==false){
			System.err.println("ERROR: Missing mandatory argument: -i <blast_file> or -d <blast_directory>");
			return;
		}
		if (line.hasOption("f")==false){
			System.err.println("ERROR: Missing mandatory argument: -f <result_file>");
			return;
		}

		if (line.hasOption("i")){
			inFile = line.getOptionValue("i");
			if (new File(inFile).exists()==false){
				System.err.println("ERROR: BLAST file not found: "+inFile);
				return;
			}
			// prepare outfile
			if (line.hasOption("o")==false){
				outFile = inFile+"_filtered";
			}
			else{
				outFile = line.getOptionValue("o");
			}
		}
		if (line.hasOption("d")){
			scanSingleFile=false;
			directory = line.getOptionValue("d");
			if (new File(directory).exists()==false){
				System.err.println("ERROR: BLAST directory not found: "+directory);
				return;
			}
			if (new File(directory).isDirectory()==false){
				System.err.println("ERROR: BLAST directory not a directory: "+directory);
				return;
			}
			if (line.hasOption("p")){
				pattern = line.getOptionValue("p");
			}
		}
		filterFile = line.getOptionValue("f");
		if (new File(filterFile).exists()==false){
			System.err.println("ERROR: Filter file not found: "+filterFile);
			return;
		}

		if (verbose){
			System.out.println("Start filtering:");
		}

		// load the filter from a file
		BFilter filter;
		try {
			filter = BlastFilterTool.readFilter(filterFile, verbose);
		} catch (Exception e) {
			System.err.println("  ERROR: unable to read filter file.");
			System.err.println("  "+e.toString());
			return;
		}
		//apply the filter on the data
		if (verbose){
			dumpFilter(filter);
		}
		if (scanSingleFile){
			applyFilter(inFile, outFile, filter, verbose);
		}
		else{
			applyFilter(directory, pattern, outFile, filter, verbose);
		}
	}

}
