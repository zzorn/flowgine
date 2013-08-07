package org.skycastle.flowgine.utils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Utilities for working with files.
 */
public class FileUtils {

    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    /**
     * @param file file to read.
     * @return contents of the file as text, assumes content is in UTF-8 format.
     * Throws an illegal argument exception if the file was not found or could not be read.
     */
    public static String readFile(File file) {
        try {
            return readStream(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file '" + file + "': " + e.getMessage(), e);
        }
    }

    /**
     * @param resourcePath path to resource to read
     * @return contents of the resource as text, assumes content is in UTF-8 format.
     * Throws an illegal argument exception if the resource was not found or could not be read.
     */
    public static String readResource(String resourcePath) {
        try {
            return readStream(FileUtils.class.getResourceAsStream("/" + resourcePath.replace('\\', '/')));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read resource '" + resourcePath + "': " + e.getMessage(), e);
        }
    }

    /**
     * @return contents of the stream as text, assumes content is in UTF-8 format.
     * @throws IOException if there was some problem.
     */
    public static String readStream(InputStream reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reader, UTF8_CHARSET));
        try {
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                builder.append(line);
                builder.append('\n');
                line = bufferedReader.readLine();
            }

            return builder.toString();
        } finally {
            bufferedReader.close();
        }
    }




}
