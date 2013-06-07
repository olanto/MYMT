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

import org.docx4j.jaxb.Context;
import org.docx4j.wml.*;

/**
 * Some useful methods.
 */
public class Utils {

    /**
     * Create a new run with the properties of the run given
     * in parameter.
     * @param originalRun the run to copy.
     * @return the new run.
     */
    public static final R copyRun(R originalRun) {
        R run = Context.getWmlObjectFactory().createR();
        run.setRPr(originalRun.getRPr());
        //TODO good way to copy a run ?
        run.setRsidRPr(originalRun.getRsidRPr());
        run.setRsidR(originalRun.getRsidR());
        run.setRsidDel(originalRun.getRsidDel());
        return run;
    }

    /**
     * Create a new run with the RPr of the given paragraph.
     * @param p the paragraph which contains the wanted properties.
     * @return the new run.
     */
    public static R createRunWithPPr(P p) {
        R run = Context.getWmlObjectFactory().createR();
        //TODO create RPr for the new run ! good way to do it ?
        if (p.getPPr() != null && p.getPPr().getRPr() != null) {
            run.setRPr(paraRPrToRPr(p.getPPr().getRPr()));
        }
        run.setRsidRPr(p.getRsidRPr());
        return run;
    }

    /**
     * Create a new Text with with given parameters.
     * @param textContent the content text which will be displayed.
     * @param preserve can take two values : "preserve" or null.
     *          if null, the leading and trailing spaces will not be displayed.
     * @return the created Text.
     */
    public static Text createText(String textContent, String preserve) {
        Text text = Context.getWmlObjectFactory().createText();
        text.setValue(textContent);
        text.setSpace(preserve);
        return text;
    }

    private static RPr paraRPrToRPr(ParaRPr paraRPr) {
        RPr rPr = Context.getWmlObjectFactory().createRPr();
        rPr.setB(paraRPr.getB());
        rPr.setBCs(paraRPr.getBCs());
        rPr.setBdr(paraRPr.getBdr());
        rPr.setCaps(paraRPr.getCaps());
        rPr.setColor(paraRPr.getColor());
        rPr.setCs(paraRPr.getCs());
        rPr.setDstrike(paraRPr.getDstrike());
        rPr.setEastAsianLayout(paraRPr.getEastAsianLayout());
        rPr.setEffect(paraRPr.getEffect());
        rPr.setEm(paraRPr.getEm());
        rPr.setEmboss(paraRPr.getEmboss());
        rPr.setFitText(paraRPr.getFitText());
        rPr.setHighlight(paraRPr.getHighlight());
        rPr.setI(paraRPr.getI());
        rPr.setICs(paraRPr.getICs());
        rPr.setImprint(paraRPr.getImprint());
        rPr.setKern(paraRPr.getKern());
        rPr.setLang(paraRPr.getLang());
        rPr.setNoProof(paraRPr.getNoProof());
        rPr.setOMath(paraRPr.getOMath());
        rPr.setOutline(paraRPr.getOutline());
        rPr.setParent(paraRPr.getParent());
        rPr.setPosition(paraRPr.getPosition());
        rPr.setRFonts(paraRPr.getRFonts());
        //rPr.setRPrChange();
        rPr.setRStyle(paraRPr.getRStyle());
        rPr.setRtl(paraRPr.getRtl());
        rPr.setShadow(paraRPr.getShadow());
        rPr.setShd(paraRPr.getShd());
        rPr.setSmallCaps(paraRPr.getSmallCaps());
        rPr.setSnapToGrid(paraRPr.getSnapToGrid());
        rPr.setSpacing(paraRPr.getSpacing());
        rPr.setSpecVanish(paraRPr.getSpecVanish());
        rPr.setStrike(paraRPr.getStrike());
        rPr.setSz(paraRPr.getSz());
        rPr.setSzCs(paraRPr.getSzCs());
        rPr.setU(paraRPr.getU());
        rPr.setVanish(paraRPr.getVanish());
        rPr.setVertAlign(paraRPr.getVertAlign());
        rPr.setW(paraRPr.getW());
        rPr.setWebHidden(paraRPr.getWebHidden());
        return rPr;
    }
}
