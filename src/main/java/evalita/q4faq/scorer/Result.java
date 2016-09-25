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

import java.util.Objects;

/**
 *
 * @author qa4faq
 */
public class Result implements Comparable<Result> {

    private String qeryId;

    private String faqId;

    private double score;

    public Result() {
    }

    public Result(String qeryId, String faqId) {
        this.qeryId = qeryId;
        this.faqId = faqId;
    }

    public Result(String qeryId, String faqId, double score) {
        this.qeryId = qeryId;
        this.faqId = faqId;
        this.score = score;
    }

    

    public String getQeryId() {
        return qeryId;
    }

    public void setQeryId(String qeryId) {
        this.qeryId = qeryId;
    }

    public String getFaqId() {
        return faqId;
    }

    public void setFaqId(String faqId) {
        this.faqId = faqId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.qeryId);
        hash = 79 * hash + Objects.hashCode(this.faqId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Result other = (Result) obj;
        if (!Objects.equals(this.qeryId, other.qeryId)) {
            return false;
        }
        if (!Objects.equals(this.faqId, other.faqId)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Result o) {
        return Double.compare(score, o.score);
    }

}
