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
 *
 * @author pierpaolo
 */
public class Results2treceval {

    /**
     * TREC_EVAL format: qid iter docno rank sim run_id
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));
                BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
                int i = -1;
                int r = 1;
                String qid = "#";
                while (reader.ready()) {
                    String[] split = reader.readLine().split("\\s+");
                    if (!split[0].equals(qid)) {
                        i++;
                        qid = split[0];
                        r = 1;
                    }
                    writer.append(split[0]).append("\t").append("Q" + i).append("\t").append(split[1])
                            .append("\t").append(String.valueOf(r)).append("\t").append(split[2]).append("\t").append("qa4faq");
                    writer.newLine();
                    r++;
                }
                reader.close();
                writer.close();
            } else {
                System.err.println("Number of arguments not valid");
                System.err.println("Qrel2treceval qrel_qa4faq qrel_trec");
                System.exit(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(Results2treceval.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
        }
    }

}
