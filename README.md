#BLAST Filter Tool

[![License](https://img.shields.io/badge/license-Affero%20GPL%203.0-blue.svg)](https://www.gnu.org/licenses/agpl-3.0.txt)

##Introduction

This project contains a Java software implementing a tool capable of filtering [NCBI BLAST](http://blast.ncbi.nlm.nih.gov/Blast.cgi) results with user-defined constraints. 

BLAST Filter Tool (BFT) contains:

  * the filtering engine to be used as a command-line tool
  * a graphical filter editor to create filters in a very straightforward way without the need to use any obscure query language

While first designed to handle NCBI BLAST results, BFT is also capable of working on [INRIA PLAST](http://plast.inria.fr/) results or any other software packages capable of producing XML data files conform to [NCBI_BlastOutput.dtd](http://www.ncbi.nlm.nih.gov/data_specs/dtd/NCBI_BlastOutput.dtd).

##Requirements

Use a [Java Virtual Machine](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 1.7 (or above) from Oracle. 

*Not tested with any other JVM providers but Oracle... so there is no guarantee that the software will work as expected if not using Oracle's JVM.*

##Working with the binary release

The most easy way to use BLAST Filter Tool consists in downloading an official release. It is made of a single Java binary archive file:

    blast-filter-tool-x.y.z.jar

It is available from this Github page: check [Releases] tab and download the latest release.

We invite all users to start working with that tool, as explained in the next section. 

*Notice for developers:* this unique JAR file is absolutely not intended to be embedded within any other applications since it contains all the dependencies (third-party librairies) of BFT into a single large file. So, if you want to include BFT into some other softwares, start from its source code.

##The user manual

BLAST Filter Tool (BFT) is a very easy-to-use command line tool. Its general use is as follows:


    usage: java -jar blast-filter-tool-X.Y.jar <args>
     -d <arg>   Directory with BLAST result files, legacy XML formated
                [mandatory]. Exclusive with '-i'.
     -e         launch the Filter Editor
     -f <arg>   filter file [mandatory]
     -h         print this message
     -i <arg>   Single BLAST result file, legacy XML formated [mandatory].
                Exclusive with '-d'.
     -n         verbose mode off; default in on
     -o <arg>   result file
     -p <arg>   Pattern to locate BLAST files in directory. Default is '*.*'.
                Use with '-d'.

So, first of all, you need two "things" to use BFT:

* a filter
* a BLAST result file (XML format)

###What is a filter?

A filter is made of some rules, each of them being on constraint applied on the data contains in a BLAST result file. 

For instance, a BLAST file provides scores, e-values, hit definitions, alignment lengths, etc. Using BFT, you can create specific contraints to retain only hits satisfying these criteria.

###What is an XML BLAST result file?

BFT is capable of reading legacy NCBI XML file. This is the result file you can create when using the following argument of BLAST+:

    -outfmt 5 

For those of you that are still using the legacy BLAST, use the argument:

    -m 7

And for those of you that are using [PLAST](http://plast.inria.fr/), use the argument:

    -m 4

###How to create a filter?

Use the filter editor provided with this software. Start it as follows:

      java -jar blast-filter-tool-5.0.0.jar -e

The first time you start the Filter Editor, you'll notice that it provides some sample filters. You can adapt them or create new ones to meet your requirements. 

It is also worth noting that all your filters are automatically saved by the software, so that you never loose them (storage path is: \<your\_home\_directory\>/.bft_filter).

When done, look at the bottom of the Editor Main Frame: you'll see the path to the file containg the filter, and you're going to use it as follows.

###How to filter a result?

On the revious step, you've created a Filter that is stored in some file. Now, you use that filter file as follows:

     java -jar blast-filter-tool-5.0.0.jar -i <blast_xml_file> -f <filter_file>

     with:
        <blast_xml_file>: path to the BLAST XML result file
        <filter_file>   : path to the filter file
        (it is usually a good idea to use absolute path).

In this example, BFT stores the result in the new file called  \<blast\_xml\_file\> suffixed with '\_filtered'. If you want ot create a file with a name of your choice, simply add the following argument to  the previous command: 

      ...   -o <filtered_result_file>

*Notice:* when a filter does not retain any hits, no result file is created.

###How to filter several results?

Of course, you could use procedure 'C' to setup a shell script when you have to process several BLAST results. 

However, there is no need for such a script: BFT comes with specific arguments to deal with multiple BLAST results processing, as follows:

    java -jar ... -d <blast_directory> -f <filter_file>

    with:
       <blast_directory>: path to a directory containing BLAST result files
       <filter_file>    : path to the filter file
       (it is usually a good idea to use absolute path).

By default, that command will process ALL files contained in the provided directory. If you want to only process particular files that can be identified using a regular expression, use this:

    java -jar ... -d <blast_directory> -p "<reg_exp>" -f <filter_file>

    with:
       <blast_directory>: path to a directory containing BLAST result files
       <reg_exp>        : regular expression between double quotes
       <filter_file>    : path to the filter file
        (it is usually a good idea to use absolute path).

Example: 

    java -jar ... -d my_results -p "blast*.xml" ...

will only process all files matching 'blast*.xml' in directory 'my_results'.

All filtered results are saved in a file having a name made of the original BLAST file name suffixed with '_filtered'. When using '-d', you cannot use  option '-o'.
   
###How to turn off verbose mode?

By default, the tool tells you what it does. Use this argument to turn off verbose mode:

      ...   -n

###Make a test!

So, let's make a try with test data. 

First, put the 'blast-filter-tool-5.0.0.jar' file into a directory. Then put in the same directory the files 'blastp.xml' and 'filter1.xml' available in the 'test' directory of this project.

Now, start a job:

    java -jar blast-filter-tool-5.0.0.jar -i blastp.xml -f filter.xml -o out.xml
    
    Start filtering:
      read filter: filter1.xml
        filter is: HSP E-Value < 0.001
      read input file: blastp.xml
        content: 1 iteration ; 19 hits ; 20 HSPs
      filtering done
        content: 1 iteration ; 15 hits ; 16 HSPs
        writing file: out.xml

And you'll have a file called 'out.xml': the results of the filtering of 'blastp.xml'.

###Memory (RAM) issues

BLAST XML file can be very huge. So, you could have to request the Java Virtual Machine to play with more memory using well known JVM arguments: -Xmx and -Xms.

For instance, the following command starts BFT with 256Mb of memory and will allow the process to use up to 2G of memory:

    java -Xms256m -Xmx2G -jar blast-filter-tool-5.0.0.jar

##BFT: develop using an IDE or Ant

I use Eclipse for development purpose, so BFT can be imported in that IDE (see dependencies, below).

Otherwise, you can use any other Java IDE and/or work on the command-line: a 'build.xml' for Ant 1.7+ is provided. 

##License and dependencies

BFT itself is released under the GNU Affero General Public License, Version 3.0. [AGPL](https://www.gnu.org/licenses/agpl-3.0.txt)

BFT depends on several thrid-party libraries as stated in the NOTICE.txt file provided with this project.

##A short story of BLAST Filter Tool (2006-today)

BLAST Filter Tool was started on october 2006. It is a concrete implementation of the Hyper-Graph Explorer (HGE) data modeling and querying system applied on sequence data (DNA, RNA and proteins). For more information on HGE, check [this project]().

From 2007 to 2015, it was licensed to the company Korilog to become the core of the data modeling and querying system of two other softwares I designed: [KoriBlast and ngKLAST](http://plealog.com/korilog/product-sheets/ngKLAST.pdf).

Finally, when Korilog ceased its activities on June 2015, I decided to release BFT source code to the open source community. I think (I hope) it will be of general interest for the bioinformatics community since working with BLAST results remain a *de facto* standard task when analysing sequence data. 

--
(c) 2006-2016 - Patrick G. Durand
