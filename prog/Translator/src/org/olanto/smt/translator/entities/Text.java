/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myMT.

   myMT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myMT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myMT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.smt.translator.entities;

import org.olanto.smt.docx.DocxHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.olanto.smt.lineseg.FileSegmentationMany;
import org.olanto.smt.translator.Config;
import org.olanto.smt.translator.Tokenizer;
import org.olanto.smt.xliff.UploadedFile;
import org.olanto.smt.xliff.Xliff;

import static org.olanto.smt.translator.Config.*;
import org.olanto.util.smt.utilsmt.SenseOS;
/**
 *
 */
public class Text {

    private final String corpus;
    private final String source;
    private final String target;
    private Collection<Sentence> sentences;
    private final UploadedFile upfile;
    private final boolean fromfile;
    private Xliff xliff;
    private DocxHandler docx;
    private static final String FILETEMPO = SenseOS.getMYMT_HOME() +"/TEMP/tempodocx.docx";
    private static final String FILETEMPO_OUT = SenseOS.getMYMT_HOME() +"/TEMP/outdocx.docx";

    /**
     * in the case where we are an upper caser (to propaged information
     * @param corpus
     * @param source
     * @param target
     * @param sentences
     */
    public Text(String corpus, String source, String target, Collection<Sentence> sentences,
            UploadedFile upfile, boolean fromfile, Xliff xliff, DocxHandler docx) {
        this.corpus = corpus;
        this.source = source;
        this.target = target;
        this.sentences = sentences;
        this.upfile = upfile;
        this.fromfile = fromfile;
        this.xliff = xliff;
        this.docx = docx;
    }

    /**
     * in the case where we are not using file
     * @param corpus
     * @param source
     * @param target
     * @param sentences
     */
    public Text(String corpus, String source, String target, Collection<Sentence> sentences) {
        this.corpus = corpus;
        this.source = source;
        this.target = target;
        this.sentences = sentences;
        upfile = null;
        fromfile = false;
    }

    /**
     * in the case where we are using file
     * @param corpus
     * @param source
     * @param target
     * @param sentences
     */
    public Text(String corpus, String source, String target, UploadedFile upfile) {
        this.corpus = corpus;
        this.source = source;
        this.target = target;
        this.upfile = upfile;
        fromfile = true;
        if (upfile.isTxt()) {
            sentences = segmentSentences(upfile.getContentString());
        }
        else if (upfile.isConvertible()) {
            sentences = segmentSentences(upfile.getConvertedFile());
        }
        else if (upfile.isSdlxliff()) {
            xliff = new Xliff();
            //System.out.println("sdlxlfiff:"+upfile.getContent().substring(0, 100));
            xliff.initFromString(upfile.getContentString());
            String[] segment = xliff.getSourceSeg();
            sentences = new ArrayList();
            for (int i = 0; i < segment.length; i++) {
                sentences.add(new Sentence(i, segment[i]));
            }
        } else if (upfile.isDocx()) {
            try {
                System.out.println(Config.MSG.MSG00 + upfile.getFileName());
                UtilsFiles.byte2file(upfile.getContentBytes(),FILETEMPO );
                System.out.println(Config.MSG.MSG01 + FILETEMPO);
                docx = new DocxHandler(new File(FILETEMPO));
                System.out.println("docx loaded:" + upfile.getFileName());
                String[] segment = docx.getSourceSeg();
                sentences = new ArrayList();
            for (int i = 0; i < segment.length; i++) {
                sentences.add(new Sentence(i, segment[i]));
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sentences = null;
        }


    }

    public byte[] getDocxDoc() {

        String[] translated = new String[sentences.size()];
        int count = 0;
        for (Sentence sentence : sentences) {
            translated[count] = sentence.getTarget();
            count++;
        }
        System.out.println("docx getDocxDoc count:" + count);
        docx.setTargetSeg(translated);
        try{
        docx.save(new File(FILETEMPO_OUT));
        } catch(Exception e){
            e.printStackTrace();
        }
        return UtilsFiles.file2byte(new File(FILETEMPO_OUT));
    }

      public String getXliffDoc() {
        String[] translated = new String[sentences.size()];
        int count = 0;
        for (Sentence sentence : sentences) {
            translated[count] = sentence.getTarget();
            count++;
        }
        xliff.setTargetSeg(translated);
        return xliff.saveAsString();
    }


    public Collection<Sentence> getSentences() {
        return sentences;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public int getSize() {
        return sentences.size();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        for (Sentence sentence : sentences) {
            out.append(sentence).append("\n");
        }
        return out.toString();
    }

    public static Collection<Sentence> parseSentences(String text, String separator) {
        String[] strings = text.split(separator);
        Collection<Sentence> sentences = new ArrayList();
        for (int i = 0; i < strings.length; i++) {
            sentences.add(new Sentence(i, strings[i]));
        }
        return sentences;
    }

    public static Collection<Sentence> segmentSentences(String text) {
        FileSegmentationMany fs = new FileSegmentationMany();
        String[] strings = FileSegmentationMany.list2tab(fs.readFile(text));
        Collection<Sentence> sentences = new ArrayList();
        int lengthMax = 0;
        for (int i = 0; i < strings.length; i++) {
            if (lengthMax < strings[i].length()) {
                lengthMax = strings[i].length();
            }
           if (MAX_SENTENCE_LENGTH < strings[i].length()){
            sentences.add(new Sentence(i, WARNING_SENTENCE_LENGTH+": "+strings[i].substring(0, MAX_SENTENCE_LENGTH)));
          //  System.out.println(i+" ("+strings[i].length()+") - "+strings[i]);
            }
           else {
               sentences.add(new Sentence(i, strings[i]));
           }
        }
        System.out.println("segment result: #sentence:" + strings.length
                + " avgLength:" + (text.length() / Math.max(strings.length, 1))
                + " maxLength:" + lengthMax);
        return sentences;
    }

    public void tokenizeSentences() {
        for (Sentence sentence : sentences) {
            sentence.setSource(Tokenizer.process(sentence.getSource()));
        }
    }

    /**
     * @return the fromfile
     */
    public boolean isFromfile() {
        return fromfile;
    }

    /**
     * @return the upfile
     */
    public UploadedFile getUpfile() {
        return upfile;
    }

    /**
     * @return the xliff
     */
    public Xliff getXliff() {
        return xliff;
    }

    /**
     * @return the docx
     */
    public DocxHandler getDocx() {
        return docx;
    }

    { // init config
        ReadParameters();
    }

}
