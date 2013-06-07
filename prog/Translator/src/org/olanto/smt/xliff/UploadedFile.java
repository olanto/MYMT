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

package org.olanto.smt.xliff;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;
import org.olanto.convsrv.server.ConvertService;
import static org.olanto.smt.translator.Config.*;

/**
 *
 */
public class UploadedFile {

    private static final Logger _logger = Logger.getLogger(UploadedFile.class);
    private final String contentString;
    private final byte[] contentBytes;
    private final String fileName;

    public UploadedFile(String content, String fileName) {
        this.contentString = content;
        this.contentBytes = null;
        this.fileName = fileName;
    }

    public UploadedFile(byte[] content, String fileName) {
        this.contentString = null;
        this.contentBytes = content;
        this.fileName = fileName;
    }

    /**
     * @return the content
     */
    public String getContentString() {
        return contentString;
    }

    /**
     * @return the content
     */
    public String getConvertedFile() {
        
        String ret = "MYCAT: Seems not working, RMI server down?";
        _logger.info("Request to convert file: " + fileName);
        System.out.println("Request to convert file: " + fileName);

        try {
//            System.out.println("1-convert: ");
            Remote r = Naming.lookup(RMI_SERVER_CONV);
//            System.out.println("2-convert: ");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
//                System.out.println("4-convert: " + is.getInformation());

//                System.out.println("5-convert: " + fileName);
                int pos = fileName.lastIndexOf('\\');

                String tempoFileName = fileName;

                if (pos >= 0) {
                    tempoFileName = fileName.substring(pos + 1);
                }
//                System.out.println("6-convert: " + tempoFileName);
                ret = is.File2Txt(contentBytes, tempoFileName);
//                System.out.println("7-convert: " +ret);
            } else {
                return "CONVSRV Service not found or not compatible.";
            }

        } catch (NotBoundException ex) {
            _logger.error(ex);
        } catch (MalformedURLException ex) {
            _logger.error(ex);
        } catch (RemoteException ex) {
            _logger.error(ex);
        }

        return ret;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    public boolean isTxt() {
        return fileName.toLowerCase().endsWith(".txt");
    }

    public boolean isSdlxliff() {
        return fileName.toLowerCase().endsWith(".sdlxliff");
    }

    public boolean isDocx() {
        return fileName.toLowerCase().endsWith(".docx");
    }

    public boolean isConvertible() {
        if (fileName.toLowerCase().endsWith(".doc")
                || fileName.toLowerCase().endsWith(".pdf")) {
            return true;
        }
        return false;
    }

    /**
     * @return the contentBytes
     */
    public byte[] getContentBytes() {
        return contentBytes;
    }
}
