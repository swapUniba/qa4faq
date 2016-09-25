/**
 * Copyright (c) 2016, QA4FAQ AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
 */
package evalita.q4faq.baseline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author qa4faq
 */
public class Search {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 2) {
                IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(args[0]))));
                BufferedReader reader = new BufferedReader(new FileReader(args[1]));
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]));
                String[] fields = new String[]{"question", "answer", "tag"};
                Map<String, Float> boosts = new HashMap<>();
                boosts.put("question", 4f);
                boosts.put("answer", 2f);
                boosts.put("tag", 1f);
                QueryParser parser = new MultiFieldQueryParser(fields, new ItalianAnalyzer(), boosts);
                while (reader.ready()) {
                    String[] split = reader.readLine().split("\t");
                    Query q = parser.parse(split[1].replace("?", " ").replace("!", " ").replace("/", " "));
                    TopDocs topdocs = searcher.search(q, 25);
                    for (ScoreDoc res : topdocs.scoreDocs) {
                        writer.append(split[0]).append("\t");
                        writer.append(searcher.doc(res.doc).get("id")).append("\t");
                        writer.append(String.valueOf(res.score));
                        writer.newLine();
                    }
                }
                reader.close();
                writer.close();
            } else {
                throw new IllegalArgumentException("Number of arguments not valid");
            }
        } catch (IOException | IllegalArgumentException | ParseException ex) {
            Logger.getLogger(Search.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
