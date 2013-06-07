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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class PartTraversal<T> extends Traversal {
    private final Object parent;
    private final List<T> objects = new LinkedList<T>();
    private final Class<T> type;
    private boolean deepTraversal = false;

    public static <T> PartTraversal<T> createTraversal(Class<T> type, Object parent) {
        return new PartTraversal<T>(type, parent);
    }

    private PartTraversal(Class<T> _type, Object _parent) {
        this.type = _type;
        this.parent = _parent;
    }

    public Collection<T> getObjects() {
        objects.clear();
        walkJAXBElements(parent);
        return objects;
    }

    /**
     * Set the deepTraversal option. If <code>false</code>, the traversal
     * will not continue when it finds an object with the wanted type.
     * The default value is <code>false</code>.
     * @param deepTraversal the new value for the <code>deepTraversal</code> option.
     */
    public void setDeepTraversal(boolean deepTraversal) {
        this.deepTraversal = deepTraversal;
    }

    public boolean isDeepTraversal() {
        return deepTraversal;
    }

    @Override
    protected void apply(Object o) {
        if (type.isAssignableFrom(o.getClass())) {
            this.objects.add((T) o);
        }
    }

    @Override
    protected boolean shouldTraverse(Object o) {
        if (deepTraversal) {
            return true;
        } else {
            return !(type.isAssignableFrom(o.getClass()));
        }
    }

}
