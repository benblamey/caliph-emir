/*
 * This file is part of the LIRe project: http://www.semanticmetadata.net/lire
 * LIRe is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LIRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LIRe; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * We kindly ask you to refer the following paper in any publication mentioning Lire:
 *
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval –
 * An Extensible Java CBIR Library. In proceedings of the 16th ACM International
 * Conference on Multimedia, pp. 1085-1088, Vancouver, Canada, 2008
 *
 * http://doi.acm.org/10.1145/1459359.1459577
 *
 * Copyright statement:
 * ~~~~~~~~~~~~~~~~~~~~
 * (c) 2002-2011 by Mathias Lux (mathias@juggle.at)
 *     http://www.semanticmetadata.net/lire
 */

package net.semanticmetadata.lire.utils;

import net.semanticmetadata.lire.ImageSearchHits;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This file is part of the Caliph and Emir project: http://www.SemanticMetadata.net
 * <br>Date: 04.02.2006
 * <br>Time: 09:44:49
 *
 * @author Mathias Lux, mathias@juggle.at
 */
public class FileUtils {
    /**
     * Returns all jpg images from a directory in an array.
     *
     * @param directory                 the directory to start with
     * @param descendIntoSubDirectories should we include sub directories?
     * @return an ArrayList<String> containing all the files or nul if none are found..
     * @throws IOException
     */
    public static ArrayList<String> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<String> resultList = new ArrayList<String>(256);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) && !file.getName().startsWith("tn_")) {
                resultList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<String> tmp = getAllImages(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return null;
    }

    /**
     * Returns all jpg images from a directory in an array.
     *
     * @param directory                 the directory to start with
     * @param descendIntoSubDirectories should we include sub directories?
     * @return an ArrayList<File> containing all the files or nul if none are found..
     * @throws IOException
     */
    public static ArrayList<File> getAllImageFiles(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<File> resultList = new ArrayList<File>(256);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) && !file.getName().startsWith("tn_")) {
                resultList.add(file);
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<File> tmp = getAllImageFiles(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return null;
    }

    public static void saveImageResultsToHtml(String prefix, ImageSearchHits hits, String queryImage) throws IOException {
        long l = System.currentTimeMillis() / 1000;
        BufferedWriter bw = new BufferedWriter(new FileWriter("results-" + prefix + "-" + l + ".html"));
        bw.write("<html>\n" +
                "<head><title>Search Results</title></head>\n" +
                "<body bgcolor=\"#FFFFFF\">\n");
        bw.write("<h3>query</h3>\n");
        bw.write("<a href=\"file://" + queryImage + "\"><img src=\"file://" + queryImage + "\"></a><p>\n");
        bw.write("<h3>results</h3>\n");
        for (int i = 0; i < hits.length(); i++) {
            bw.write(hits.score(i) + " - <a href=\"file://" + hits.doc(i).get("descriptorImageIdentifier") + "\"><img src=\"file://" + hits.doc(i).get("descriptorImageIdentifier") + "\"></a><p>\n");
        }
        bw.write("</body>\n" +
                "</html>");
        bw.close();
    }

    public static void zipDirectory(File directory, File base, ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zipDirectory(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }


}
