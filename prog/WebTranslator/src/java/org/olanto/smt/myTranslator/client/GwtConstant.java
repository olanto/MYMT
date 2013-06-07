/**
 * ********
 * Copyright Â© 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.smt.myTranslator.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * pour stocker les propriÃ©tÃ©s
 */
public class GwtConstant implements IsSerializable {

    /**
     * client basic parameters
     * **********************************************************************************
     */
    public int EXP_DAYS;  // durée des cookies
    public String EXTENSION_SET;// listes des extension à  disposition
    public String CORPUS_SET;  // listes corpus à  disposition
    public String LANG_SET;// listes des langues à  disposition
    public String INTERFACE_MESSAGE_PATH;// chemin pour les messages
    public String INTERFACE_MESSAGE_LANG;// langue de l'utilisateur
}
