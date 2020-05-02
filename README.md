# Search Engine implementation using Apache Lucene 8.4.1

First run the Indexer.

This will create index files under `src/main/resources`.

Run QueryParser_And_Searcher to parse queries and search them in the index created.

Results file will be created under src/main/resources/trec_eval-9.0.7/results.txt

Check the Accuracy of your search using Trec_eval :

Download the Trec_eval file from the internet.

Run this command in your command prompt/terminal,inside the trec_eval folder:

./trec_eval qrels.assignment2.part1 results.txt
