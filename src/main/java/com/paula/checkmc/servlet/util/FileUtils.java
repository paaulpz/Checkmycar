/**
 * 
 */
package com.paula.checkmc.servlet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtils {

    public static int read(String filePath, byte[] buffer) throws IOException {
        File f = new File(filePath);
        InputStream fis = new FileInputStream(f);
        int bytesLeidos = fis.read(buffer);
        fis.close();
        return bytesLeidos;
    }

    public static void write(String filePath, byte[] data) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStream os = new FileOutputStream(file);
        os.write(data);
        os.flush();
        os.close();
    }
}
