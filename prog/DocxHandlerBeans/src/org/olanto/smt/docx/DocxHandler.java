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
package org.olanto.smt.docx;

import org.docx4j.model.structure.HeaderFooterPolicy;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.olanto.smt.traversal.PartTraversal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class DocxHandler {

    private final WordprocessingMLPackage wordMLPackage;
    private final MainDocumentPart mainPart;

    private List<Paragraph> modifications = null;
    
    /**
     * Create a new instance of <code>DocxHandler</code>.
     * @param file the docx file to read
     * @throws Docx4JException if an error occurs during the file reading
     */
    public DocxHandler(File file) throws Docx4JException {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null.");
        }
        this.wordMLPackage = WordprocessingMLPackage.load(file);
        this.mainPart = wordMLPackage.getMainDocumentPart();
    }

    /**
     * Save the current state of the docx to the specified location.
     * @param file The writing location.
     * @throws Docx4JException if an error occurs during file writing.
     */
    public void save(File file) throws Docx4JException {
        if (file == null) {
            throw new IllegalArgumentException("file cannot be null.");
        }
        wordMLPackage.save(file);
    }


    public String[] getSourceSeg() {
        // Get the document parts
        Collection<Object> parts = getPartElements();

        // The list of all the modified paragraphs
        modifications = new ArrayList<Paragraph>();

        // for each part, run the part traversal :
        for (Object part : parts) {
            PartTraversal<P> partTraversal = PartTraversal.createTraversal(P.class, part);
            partTraversal.setDeepTraversal(true);
            Collection<P> paragraphs = partTraversal.getObjects();

            // for each paragraph create an object Paragraph and run the parser :
            for (P p : paragraphs) {
                Paragraph paragraphModif = new Paragraph(p);
                paragraphModif.parse();

                // finally check if the paragraph contains some text, and add its
                // modifications to the list
                if (paragraphModif.sizeSegment() != 0) {
                    modifications.add(paragraphModif);
                }
            }
        }

        List<String> segmentsList = new ArrayList<String>();
        for (Paragraph paragraph : modifications) {
            segmentsList.addAll(paragraph.getSegments());
        }

        String[] segments = new String[segmentsList.size()];
        segmentsList.toArray(segments);

        return segments;
    }

    public void setTargetSeg(String[] targetSeg) {
        if (targetSeg == null) {
            throw new IllegalArgumentException("targetSeg cannot be null !");
        }
        if (modifications == null) {
            throw new IllegalStateException("The method getSourceSeg() must be called before setTargetSeg()");
        }
        if (targetSeg.length != sizeSegment()) {
            throw new IllegalArgumentException("targetSeg have not the required size. Found " +
                    targetSeg.length + ", required " + sizeSegment() + ".");
        }

        // Put the target segment for each paragraph and apply the modifications
        for (int i = 0, segIndex = 0; i < modifications.size(); i++) {
            Paragraph p = modifications.get(i);
            int pSizeSegment = p.sizeSegment();

            String[] pSegments = new String[pSizeSegment];
            System.arraycopy(targetSeg, segIndex, pSegments, 0, pSizeSegment);
            p.applyModifications(pSegments);

            segIndex += pSizeSegment;
        }

    }

    /**
     * Return the number of segments in this paragraph.
     * Each segment contains some text.
     * @return the number of segments.
     */
    public int sizeSegment() {
        int sizeSegment = 0;
        for (Paragraph p : modifications) {
            sizeSegment += p.sizeSegment();
        }
        return sizeSegment;
    }

    /**
     * Return the different parts of the current document. These objects
     * can be used with <code>PartTraversal</code>.
     * @return the parts of the document.
     * @see PartTraversal
     */
    private Collection<Object> getPartElements() {
        Collection<Object> parts = new LinkedList<Object>();
        parts.add(mainPart.getJaxbElement().getBody());             // main part
        if (mainPart.getCommentsPart() != null) {
            parts.add(mainPart.getCommentsPart().getJaxbElement()); // comments
        }
        if (MainDocumentPart.hasEndnotesPart(wordMLPackage)) {
            parts.add(mainPart.getEndNotesPart().getJaxbElement()); // end notes
        }
        if (MainDocumentPart.hasFootnotesPart(wordMLPackage)) {
            parts.add(mainPart.getFootnotesPart().getJaxbElement());// footnotes
        }
        // headers and footers :
        for (SectionWrapper sw : wordMLPackage.getDocumentModel().getSections()) {
            HeaderFooterPolicy hfp = sw.getHeaderFooterPolicy();

            if (hfp.getFirstHeader() != null) {
                parts.add(hfp.getFirstHeader().getJaxbElement());   // first header
            }
            if (hfp.getDefaultHeader() != null) {
                parts.add(hfp.getDefaultHeader().getJaxbElement()); // default header
            }
            if (hfp.getEvenHeader() != null) {
                parts.add(hfp.getEvenHeader().getJaxbElement());    // even header
            }
            if (hfp.getFirstFooter() != null) {
                parts.add(hfp.getFirstFooter().getJaxbElement());   // first footer
            }
            if (hfp.getDefaultFooter() != null) {
                parts.add(hfp.getDefaultFooter().getJaxbElement()); // default footer
            }
            if (hfp.getEvenFooter() != null) {
                parts.add(hfp.getEvenFooter().getJaxbElement());    // even footer
            }
        }

        return parts;
    }



    public static void main(String[] args) {


        /*
        try {
            org.docx4j.samples.OpenMainDocumentAndTraverse.main(new String[]{"./samples/doc.docx"});
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */


        try {
            File file = new File("./samples/doc.docx");
            File out = new File("./samples/out.docx");
            DocxHandler handler = new DocxHandler(file);

            String[] segments = handler.getSourceSeg();
            //System.out.println("Segments :");
            
            for (int i = 0; i < segments.length; i++) {
                //System.out.println("- " + segments[i]);
                segments[i] = "<" + segments[i] + ">";
            }

            handler.setTargetSeg(segments);

            handler.save(out);

        } catch (Docx4JException ex) {
            ex.printStackTrace();
        }
        

    }
}
