QA4FAQ
=========

This project contains source files for both the evaluation and the baseline of the QA4FAQ task at EVALITA 2016.

More details about the task are provided here: http://qa4faq.github.io/.

Evaluation
-------------

For running the evaluation script you need to compile the source code by the maven command "mvn package". After this process a folder named **target** will be created, this folder contains a jar package with all the dependencies.

For running the avalation, type the following command:
java -jar <jar_package> <qrel_file> <results_file> [-v]

The <qrel_file> is the file containing correct answers, while <results_file> is the file that you want to evaluate. The option -v enable the verbose mode during the evaluation.

Baseline
------------

The baseline is built using Apache Lucene (ver. 4.10.4). During the indexing for each FAQ a Document with four fields (id, question, answer, tag) is created. For searching a query for each question is built taking into account all the question terms.
Each field is boosted according to the following score question=4, answer=2 and tag=1. For both indexing and search the ItalianAnalyzer is adopted.

For running the indexing process, type the following command:
java -cp <jar_package> evalita.q4faq.baseline.Index <faq_csv_file> <index_folder>

For running the search process, type the following command:
java -cp <jar_package> evalita.q4faq.baseline.Search <index_folder> <questions_file> <output_file>