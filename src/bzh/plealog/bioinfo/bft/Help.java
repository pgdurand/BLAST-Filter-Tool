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

import java.io.InputStream;
import java.util.Properties;

/**
 * Help class.
 * 
 * @author Patrick G. Durand
 */
public class Help {
  private static final String SEP = "===============================================================================";
  
	protected static Properties getVersionProperties(){
		Properties  props = new Properties();
		try (InputStream in = Main.class.getResourceAsStream("version.properties");){
			props.load(in);
			in.close();
		}
		catch(Exception ex){//should not happen
			System.err.println("Unable to read props: "+ex.toString());
		}
		return props;
	}

	protected static void dumpHeader(Properties props){
    System.out.println(SEP);
	  System.out.println(props.getProperty("prg.name").toUpperCase()+". Release: "+props.getProperty("prg.version"));
    System.out.println(SEP);
	}
	protected static void dumpUsages(Properties props){
		String prgVer = props.getProperty("prg.version");
		String libName = props.getProperty("prg.name");
		String prgName = libName.toUpperCase();
		
    System.out.println("");
    System.out.println(SEP);
    System.out.println("User Manual:");
    System.out.println("");
		System.out.println("== A. How to create a filter?");
		System.out.println("");
		System.out.println("   Use the filter editor provided with this software. Start it as follows:");
		System.out.println("");
		System.out.println("      java -jar "+libName+"-"+prgVer+".jar -e");
		System.out.println("");
		System.out.println("   Then create a filter that meets your requirements. When done, look at the");
		System.out.println("   bottom of the Editor Main Frame: you'll see the path to the file containg ");
		System.out.println("   the filter, and you're going to use it as follows.");
		System.out.println("");
		System.out.println("== B. How to create an XML formatted BLAST result file?");
		System.out.println("");
		System.out.println("   This tool is only capable of reading NCBI Blast XML results prepared using");
		System.out.println("   argument: ");
		System.out.println("");
		System.out.println("      -outfmt 5");
		System.out.println("");
		System.out.println("   available in BLAST+ suite of softwares. So, you have to use that format");
		System.out.println("   to filter results using '"+prgName+"'.");
		System.out.println("");
		System.out.println("   For those of you that are still using the legacy BLAST, use the argument:");
		System.out.println("      -m 7");
		System.out.println("");
		System.out.println("   And for for those of you that are using PLAST, use the argument:");
		System.out.println("      -m 4");
		System.out.println("");
		System.out.println("== C. How to filter a single result?");
		System.out.println("");
		System.out.println("   During step 'A', you've created a Filter that is stored in some file. Now,");
		System.out.println("   you use that filter file as follows:");
		System.out.println("");
		System.out.println("     java -jar "+libName+"-"+prgVer+".jar -i <blast_xml_file> -f <filter_file>");
		System.out.println("");
		System.out.println("     with:");
		System.out.println("        <blast_xml_file>: path to the BLAST XML result file");
		System.out.println("        <filter_file>   : path to the filter file");
		System.out.println("        (it is usually a good idea to use absolute path).");
		System.out.println("");
		System.out.println("   In this example, "+prgName+" stores the result in the new file called");
		System.out.println("   <blast_xml_file> suffixed with '_filtered'. If you want ot create a ");
		System.out.println("   file with a name of your choice, simply add the following argument to ");
		System.out.println("   the previous command: ");
		System.out.println("");
		System.out.println("      ...   -o <filtered_result_file>");
		System.out.println("");
		System.out.println("   Notice: when a filter does not retain any hits, no result file is created.");
		System.out.println("");
		System.out.println("== D. How to filter several results?");
		System.out.println("");
		System.out.println("   Of course, you could use procedure 'C' to setup a shell script when you have");
		System.out.println("   to process several BLAST results.");
		System.out.println("");
		System.out.println("   However, there is no need for such a script: BFT comes with specific arguments");
		System.out.println("   to deal with multiple BLAST results processing, as follows:");
		System.out.println("");
		System.out.println("      java -jar ... -d <blast_directory> -f <filter_file>");
		System.out.println("");
		System.out.println("     with:");
		System.out.println("        <blast_directory>: path to a directory containing BLAST result files");
		System.out.println("        <filter_file>    : path to the filter file");
		System.out.println("        (it is usually a good idea to use absolute path).");
		System.out.println("");
		System.out.println("   By default, that command will process ALL files contained in the provided");
		System.out.println("   directory. If you want to only process particular files that can be identified");
		System.out.println("   using a regular expression, use this:");
		System.out.println("");
		System.out.println("      java -jar ... -d <blast_directory> -p \"<reg_exp>\" -f <filter_file>");
		System.out.println("");
		System.out.println("     with:");
		System.out.println("        <blast_directory>: path to a directory containing BLAST result files");
		System.out.println("        <reg_exp>        : regular expression between double quotes");
		System.out.println("        <filter_file>    : path to the filter file");
		System.out.println("        (it is usually a good idea to use absolute path).");
		System.out.println("");
		System.out.println("   Example: ");
		System.out.println("");
		System.out.println("      java -jar ... -d my_results -p \"blast*.xml\" ...");
		System.out.println("");
		System.out.println("   will only process all files matching 'blast*.xml' in directory 'my_results'.");
		System.out.println("");
		System.out.println("   All filtered results are saved in a file having a name made of the original");
		System.out.println("   BLAST file name suffixed with '_filtered'. When using '-d', you cannot use ");
		System.out.println("   option '-o'.");
		System.out.println("");
		System.out.println("== E. How to turn off verbose mode?");
		System.out.println("");
		System.out.println("   By default, the tool tells you what it does. Use this argument to turn off");
		System.out.println("   verbose mode:");
		System.out.println("");
		System.out.println("      java -jar ...   -n");
		System.out.println("");
    System.out.println(SEP);
		System.out.println(props.getProperty("prg.name").toUpperCase()+". Release: "+props.getProperty("prg.version"));
		System.out.println(" Project home: "+props.getProperty("prg.url"));
		System.out.println(" "+props.getProperty("prg.copyright"));
		System.out.println(" "+props.getProperty("prg.license"));
    System.out.println(SEP);
	}

}
