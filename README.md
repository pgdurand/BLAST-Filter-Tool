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

It is available in the [Wiki](https://github.com/pgdurand/BLAST-Filter-Tool/wiki) of this project.

##BFT: develop using an IDE or Ant

I use Eclipse for development purpose, so BFT can be imported in that IDE (see dependencies, below).

Otherwise, you can use any other Java IDE and/or work on the command-line: a 'build.xml' for Ant 1.7+ is provided. 

##License and dependencies

BFT itself is released under the GNU Affero General Public License, Version 3.0. [AGPL](https://www.gnu.org/licenses/agpl-3.0.txt)

BFT depends on several thrid-party libraries as stated in the NOTICE.txt file provided with this project.

##A short story of BLAST Filter Tool (2006-today)

BLAST Filter Tool was started on october 2006. It is a concrete implementation of the Hyper-Graph Explorer (HGE) data modeling and querying system applied on sequence data (DNA, RNA and proteins). For more information on HGE, check [this project](https://github.com/pgdurand/Hyper-Graph-Explorer).

From 2007 to 2015, it was licensed to the company Korilog to become the core of the data modeling and querying system of two other softwares I designed: [KoriBlast and ngKLAST](http://plealog.com/korilog/product-sheets/ngKLAST.pdf).

Finally, when Korilog ceased its activities on June 2015, I decided to release BFT source code to the open source community. I think (I hope) it will be of general interest for the bioinformatics community since working with BLAST results remain a *de facto* standard task when analysing sequence data. 

--
(c) 2006-2016 - Patrick G. Durand
