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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author qa4faq
 */
public class Index {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                Reader in = new FileReader(args[0]);
                IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new ItalianAnalyzer());
                IndexWriter writer = new IndexWriter(FSDirectory.open(new File(args[1])), config);
                Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withDelimiter(';').parse(in);
                for (CSVRecord record : records) {
                    int id = Integer.parseInt(record.get("id"));
                    String question = record.get("question");
                    String answer = record.get("answer");
                    String tag = record.get("tag");
                    Document doc = new Document();
                    doc.add(new StringField("id", String.valueOf(id), Field.Store.YES));
                    doc.add(new TextField("question", question, Field.Store.NO));
                    doc.add(new TextField("answer", answer, Field.Store.NO));
                    doc.add(new TextField("tag", tag.replace(",", " "), Field.Store.NO));
                    writer.addDocument(doc);
                }
                writer.close();
            } else {
                throw new IllegalArgumentException("Number of arguments not valid");
            }
        } catch (IOException | IllegalArgumentException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
