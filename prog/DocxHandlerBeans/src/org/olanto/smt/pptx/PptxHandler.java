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
package org.olanto.smt.pptx;

import org.docx4j.XmlUtils;
import org.docx4j.dml.CTRegularTextRun;
import org.docx4j.dml.CTTextParagraph;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PresentationML.MainPresentationPart;
import org.docx4j.openpackaging.parts.PresentationML.SlidePart;
import org.pptx4j.pml.Shape;
import org.olanto.smt.traversal.PartTraversal;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class PptxHandler {

    public static void main(String[] args) {

        try {
            //org.pptx4j.samples.TraverseSlide.main(new String[]{"./samples/dia.pptx"});

            PresentationMLPackage pres = PresentationMLPackage.load(new File("./samples/dia.pptx"));

            MainPresentationPart main = pres.getMainPresentationPart();


            Collection<Part> parts = pres.getParts().getParts().values();

            Collection<Object> slides = new LinkedList<Object>();

            for (Part part : parts) {
                System.out.println(part.getPartName());
                if (part instanceof SlidePart) {
                    slides.addAll(((SlidePart) part).getJaxbElement().getCSld().getSpTree().getSpOrGrpSpOrGraphicFrame());
                }
            }


            for (Object s : slides) {
                System.out.println("s: " + s);
            }


            PartTraversal<CTTextParagraph> t1 = PartTraversal.createTraversal(CTTextParagraph.class, slides);
            for (CTTextParagraph s : t1.getObjects()) {
                System.out.println("++" + s);
            }



            PartTraversal<Shape> traversal = PartTraversal.createTraversal(Shape.class, slides);
            traversal.setDeepTraversal(true);
            for (Shape s : traversal.getObjects()) {
                System.out.println(s);
                List<CTTextParagraph> pList = s.getTxBody().getP();
                for (CTTextParagraph p : pList) {
                    for (Object o : p.getEGTextRun()) {
                        System.out.println(o);
                        if (o instanceof CTRegularTextRun) {
                            CTRegularTextRun run = (CTRegularTextRun) o;
                            System.out.println(run.getT());
                        }

                    }
                }

            }

            for (Object o : slides) {
                if (o instanceof Shape)
                 System.out.println(XmlUtils.marshaltoString(o, true, true, main.getJAXBContext()));
            }
            

            /*
            List<Object> list = XmlUtils.getJAXBNodesViaXPath(main.getJAXBContext().createBinder(), ((SlidePart) parts.iterator().next()).getJaxbElement().getCSld().getSpTree().getSpOrGrpSpOrGraphicFrame(), "//a:p", false);
            for(Object o : list) {
                System.out.println(o);
            }
            */





        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
