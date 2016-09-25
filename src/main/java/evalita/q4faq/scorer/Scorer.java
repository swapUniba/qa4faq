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
package evalita.q4faq.scorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author qa4faq
 */
public class Scorer {

    public static boolean verbose = false;

    private static Map<String, Set<String>> loadqrel(File file) throws IOException {
        Map<String, Set<String>> map = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            String[] split = reader.readLine().split("\t");
            Set<String> set = map.get(split[0]);
            if (set == null) {
                set = new HashSet<>();
                map.put(split[0], set);
            }
            set.add(split[1]);
        }
        if (verbose) {
            System.out.println("[QREL] Loaded " + map.size() + " queries");
        }
        return map;
    }

    private static Map<String, List<Result>> loadResults(File file) throws IOException {
        Map<String, List<Result>> map = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (reader.ready()) {
            String[] split = reader.readLine().split("\t");
            List<Result> list = map.get(split[0]);
            if (list == null) {
                list = new ArrayList<>();
                map.put(split[0], list);
            }
            list.add(new Result(split[0], split[1], Double.parseDouble(split[2])));
        }
        for (List<Result> l : map.values()) {
            Collections.sort(l, Collections.reverseOrder());
        }
        if (verbose) {
            System.out.println("[RESULTS] Loaded " + map.size() + " results");
        }
        return map;
    }

    private static double computeScore(Map<String, Set<String>> qrel, Map<String, List<Result>> results) {
        double u = 0;
        double r = 0;
        double nr = 0;
        double n = 0;
        for (String qid : qrel.keySet()) {
            List<Result> list = results.get(qid);
            if (list == null) {
                if (verbose) {
                    System.out.println("Unanswered: " + qid);
                }
                u++;
            } else if (qrel.get(qid).contains(list.get(0).getFaqId())) {
                if (verbose) {
                    System.out.println("Correct " + qid + " " + list.get(0).getFaqId());
                }
                r++;
            } else {
                nr++;
                if (verbose) {
                    System.out.println("Uncorrect: " + qid);
                }
            }
            n++;
        }
        if (verbose) {
            System.out.println("Number of queries: " + n);
            System.out.println("Correct: " + r);
            System.out.println("Uncorrect: " + nr);
            System.out.println("Unanswered: " + u);
        }
        return 1 / n * (r + u * r / n);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            try {
                Map<String, Set<String>> qrel = loadqrel(new File(args[0]));
                Map<String, List<Result>> results = loadResults(new File(args[1]));
                verbose = args.length > 2 && args[2].equals("-v");
                double score = computeScore(qrel, results);
                System.out.println("a@1 = " + score);
            } catch (IOException ex) {
                Logger.getLogger(Scorer.class.getName()).log(Level.SEVERE, "I/O Exception", ex);
            }
        } else {
            System.err.println("Number of arguments not valid");
            System.err.println("Scorer qrel_file results_file [-v]");
            System.exit(1);
        }
    }

}
