/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalita.q4faq.scorer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TREC_EVAL format: qid iter docno rel
 *
 * @author pierpaolo
 */
public class Qrel2treceval {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
                int i = -1;
                String qid = "#";
                while (reader.ready()) {
                    String[] split = reader.readLine().split("\\s+");
                    if (!split[0].equals(qid)) {
                        i++;
                        qid=split[0];
                    }
                    writer.append(split[0]).append("\t").append("Q" + i).append("\t").append(split[1]).append("\t").append("1");
                    writer.newLine();
                }
                reader.close();
                writer.close();
            } else {
                System.err.println("Number of arguments not valid");
                System.err.println("Qrel2treceval qrel_qa4faq qrel_trec");
                System.exit(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(Qrel2treceval.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        }
    }

}
