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
package org.olanto.smt.traversal;

import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;

import java.util.List;

/**
 *
 */
public abstract class Traversal {
    public static List<Object> getChildren(Object o) {
        return TraversalUtil.getChildrenImpl(o);
    }

    // traverse depth first
    protected void walkJAXBElements(Object parent) {
        List<Object> children = getChildren(parent);
        if (children != null) {
            for (Object o : children) {
                //if it's wrapped in JAXBElement
                o = XmlUtils.unwrap(o);

                this.apply(o);

                if (this.shouldTraverse(o)) {
                    this.walkJAXBElements(o);
                }
            }
        }
    }

    abstract protected void apply(Object o);
    abstract protected boolean shouldTraverse(Object o);
}