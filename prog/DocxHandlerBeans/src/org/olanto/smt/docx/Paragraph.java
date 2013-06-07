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

import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.wml.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.log4j.Level;

/**
 */
public class Paragraph {

    private final P paragraph;
    private final List<PPart> parts = new ArrayList<PPart>();
    private PPart crtPart = null;

    private final Logger logger = Logger.getLogger(Paragraph.class);

    public Paragraph(P _p) {
        this.paragraph = _p;
        this.logger.setLevel(Level.INFO);

        init();
    }

    /**
     * Return the number of parts.
     * @return the number of parts.
     */
    public int size() {
        return parts.size();
    }

    /**
     * Return the number of segments containing some text.
     * @return the number of segments.
     */
    public int sizeSegment() {
        int nbSeg = 0;
        for (PPart part : parts) {
            if (part.hasText())
                nbSeg++;
        }
        return nbSeg;
    }

    /**
     * Return the list of segments which contain some text.
     * @return the segments.
     */
    public List<String> getSegments() {
        List<String> segments = new ArrayList<String>();
        for (PPart part : parts) {
            if (part.hasText()) {
                segments.add(part.sourceSeg.toString());
            }
        }
        return segments;
    }

    /**
     * Parse the current paragraph in order to get the source segments.
     * It will also prepare the paragraph to its modification after
     * the insertion of target segments.
     */
    public void parse() {
        init();
        parse(paragraph);
    }

    private void init() {
        parts.clear();
        nextPart();
    }

    /**
     * Create a new part for this paragraph.
     */
    private void nextPart() {
        crtPart = new PPart();
        parts.add(crtPart);
    }

    private void addBeginElement(Object o) {
        crtPart.beginElements.add(o);
    }

    private void addEndElement(Object o) {
        // if the segment contains some text, add it really at the end.
        if (crtPart.hasText()) {
            crtPart.endElements.add(o);
        }
        // else put it at the beginning.
        else {
            addBeginElement(o);
        }
    }

    private void addText(String s, String preserveSpace) {
        crtPart.addText(s, preserveSpace);
    }

    private void parse(R run) {
        for (Object childWrapped : run.getContent()) {
            Object child = XmlUtils.unwrap(childWrapped);
            // Text
            // Add the corresponding text to the current segment.
            if (child instanceof Text) {
                Text text = (Text) child;
                logger.debug("Text: " + text.getValue() + ", space=\"" + text.getSpace() + "\"");
                this.addText(text.getValue(), text.getSpace());
            }
            // References
            // Not separator, put it at the end of the segment.
            else if (child instanceof R.FootnoteRef ||
                     child instanceof R.AnnotationRef ||
                     child instanceof R.CommentReference ||
                     child instanceof R.EndnoteRef ||
                     child instanceof CTFtnEdnRef) {
                logger.debug("Reference");
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addEndElement(newRun);
            }
            // Tab or Cr
            // Act as separator, create a new segment.
            else if (child instanceof R.Tab ||
                     child instanceof R.Cr ||
                     child instanceof Br) {
                logger.debug("Tab or Cr or Br");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Dates
            // Not separator, put it at the end of the segment.
            else if (child instanceof R.DayLong ||
                     child instanceof R.DayShort ||
                     child instanceof R.MonthLong ||
                     child instanceof R.MonthShort ||
                     child instanceof R.YearLong ||
                     child instanceof R.YearShort) {
                logger.debug("Date (day, month or year)");
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(child);
                this.addEndElement(newRun);
            }
            // Continuation Separator Mark (continuationSeparator)
            else if (child instanceof R.ContinuationSeparator) {
                logger.debug("Continuation Separator Mark");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Position of Last Calculated Page Break (lastRenderedPageBreak)
            else if (child instanceof R.LastRenderedPageBreak) {
                logger.debug("Continuation Separator Mark");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Non Breaking Hyphen Character (noBreakHyphen) OR
            // Optional Hyphen Character (softHyphen)
            // forget it, and just put it at the end of the segment.
            else if (child instanceof R.NoBreakHyphen ||
                     child instanceof R.SoftHyphen) {
                logger.debug("Non Breaking Hyphen Character OR Optional Hyphen Character");
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addEndElement(newRun);
            }
            // Page Number Block (pgNum)
            // Not separator, put it at the end of the segment.
            else if (child instanceof R.PgNum) {
                logger.debug("Page Number Block");
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addEndElement(newRun);
            }
            // Absolute Position Tab Character (ptab)
            else if (child instanceof R.Ptab) {
                logger.debug("Absolute Position Tab Character");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Footnote/Endnote Separator Mark (separator)
            else if (child instanceof R.Separator) {
                logger.debug("Footnote/Endnote Separator Mark");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Symbol Character (sym)
            // Not separator, put it at the end of the segment.
            else if (child instanceof R.Sym) {
                logger.debug("Symbol Character");
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addEndElement(newRun);
            }
            // DrawingML Object (drawing)
            // separator
            else if (child instanceof Drawing) {
                logger.debug("DrawingML Object");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Pict (VML Object)
            // separator
            //TODO handle it ?
            else if (child instanceof Pict) {
                logger.debug("Pict");
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
            // Default case :
            // handle it as a separator
            else {
                logger.debug("Unknown class: " + child.getClass());
                this.nextPart();
                R newRun = Utils.copyRun(run);
                newRun.getContent().add(childWrapped);
                this.addBeginElement(newRun);
            }
        }
    }

    private void parse(ContentAccessor parent) {
        List<Object> content = parent.getContent();
        for(Object childWrapped : content) {
            Object child = XmlUtils.unwrap(childWrapped);
            // Run (r)
            if (child instanceof R) {
                logger.debug("Run");
                parse((R) child);
            }
            // hyperlink
            else if (child instanceof P.Hyperlink) {
                logger.debug("Hyperlink");
                P.Hyperlink hyperlink = (P.Hyperlink) child;
                // parse recursively the hyperlink
                parse(hyperlink);

                //TODO find the hyperlink value.
                /*
                logger.error(hyperlink.);

                R run = Utils.createRunWithPPr(paragraph);
                run.setParent(parent);
                Text text = Utils.createText(hyperlink.getAnchor(), PPart.PRESERVE);
                text.setParent(run);
                run.getContent().add(text);
                this.addEndElement(run);
                */


                List<Object> hContent = hyperlink.getContent();
                // remove all the content and add a simple marker.
                hContent.clear();
                R run = Utils.createRunWithPPr(paragraph);
                run.setParent(hyperlink);
                Text text = Utils.createText("<delHyperlink>", null);
                text.setParent(run);
                run.getContent().add(text);
                hContent.add(run);

                // put the hyperlink at the end of th e segment.
                addEndElement(childWrapped);
                

            }
            // SimpleField (fldSimple)
            else if (child instanceof CTSimpleField) {
                logger.debug("CTSimpleField");
                //TODO handle CTSimpleField
                this.nextPart();
                this.addBeginElement(childWrapped);
            }
            // ProofErr
            // Deleted
            //TODO ProofErr deleted, good way ?
            else if (child instanceof ProofErr) {
                logger.debug("ProofErr : deleted");
            }
            // CommentRangeStart
            // Put comment start and stop at the end
            else if (child instanceof CommentRangeStart) {
                logger.debug("CommentRangeStart");
                this.addEndElement(childWrapped);
            }
            // CommentRangeEnd
            // Put comment start and stop at the end
            else if (child instanceof CommentRangeEnd) {
                logger.debug("CommentRangeEnd");
                this.addEndElement(childWrapped);
            }
            //TODO handle other paragraph child types ?
            // Unknown child
            else {
                logger.debug("Unknown parent child: " + child.getClass());
                this.nextPart();
                this.addBeginElement(childWrapped);
            }
        }

    }

    public void applyModifications(String[] targetSegs) {
        if (targetSegs == null) {
            throw new IllegalArgumentException("targetSegs cannot be null.");
        }
        if (targetSegs.length != this.sizeSegment()) {
            throw new IllegalArgumentException("targetSegs has not the required size. Found " +
                    targetSegs.length + ", required " + this.sizeSegment() + ".");
        }
        
        // Remove all the old contents
        List<Object> pContent = paragraph.getContent();
        pContent.clear();

        // for each segment :
        int segIndex = 0;
        for (PPart part : parts) {
            pContent.addAll(part.beginElements);
            if (part.hasText()) {
                R run = Utils.createRunWithPPr(paragraph);
                run.setParent(paragraph);

                Text text = Utils.createText(targetSegs[segIndex], part.preserveSpace);
                text.setParent(run);
                
                run.getContent().add(text);
                pContent.add(run);
                segIndex++;
            }
            pContent.addAll(part.endElements);
        }
    }

    private static class PPart {

        public static final String PRESERVE = "preserve";

        public final Collection<Object> beginElements = new LinkedHashSet<Object>();
        public final Collection<Object> endElements = new LinkedHashSet<Object>();
        private final StringBuilder sourceSeg = new StringBuilder();
        private String preserveSpace = null;

        public void addText(String text, String preserve) {
            sourceSeg.append(text);
            if (PRESERVE.equals(preserve)) {
                preserveSpace = PRESERVE;
            }
        }

        public boolean hasText() {
            return this.sourceSeg.length() != 0;
        }
    }
}
