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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
  */
public class UtilsFiles {

    public static void main(String[] args) {
        String fileName = "C:/SIMPLE/TEMP/test1.docx";
              File file = new File(fileName);
      byte[] res = file2byte(file);
        byte2file(res, "C:/SIMPLE/TEMP/copytest1.docx");

    }

    public static byte[] file2byte(File file) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            //File length
            int size = (int) file.length();
            if (size > Integer.MAX_VALUE) {
                System.out.println("File is to larger");
            }
            byte[] bytes = new byte[size];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            int read = 0;
            int numRead = 0;
            while (read < bytes.length && (numRead = dis.read(bytes, read,
                    bytes.length - read)) >= 0) {
                read = read + numRead;
            }
            System.out.println("File size: " + read);
            // Ensure all the bytes have been read in
            if (read < bytes.length) {
                System.out.println("Could not completely read: " + file.getName());
            }
            return bytes;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

       public static byte[] file2byte(InputStream fis, int size) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            if (size > Integer.MAX_VALUE) {
                System.out.println("File is to larger");
            }
            byte[] bytes = new byte[size];
            DataInputStream dis = new DataInputStream(fis);
            int read = 0;
            int numRead = 0;
            while (read < bytes.length && (numRead = dis.read(bytes, read,
                    bytes.length - read)) >= 0) {
                read = read + numRead;
            }
            System.out.println("File size: " + read);
            // Ensure all the bytes have been read in
            if (read < bytes.length) {
                System.out.println("Could not completely read: ");
            }
            return bytes;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public static void byte2file(byte[] bytes, String fileName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            out.write(bytes);
        } catch (IOException ex) {
            Logger.getLogger(UtilsFiles.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(UtilsFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static final String file2String(InputStream stream, String txt_encoding) {
        StringBuffer txt = new StringBuffer("");
        try {
            InputStreamReader isr = new InputStreamReader(stream, txt_encoding);
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }
            return txt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}


