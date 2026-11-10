
package com.paula.checkmc.servlet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtilsTest {

    public void write(byte[] a, String fileName, boolean append) throws IOException {
        FileOutputStream fos = null;
        File f = null;
        try {
            f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            fos = new FileOutputStream(f, true);
            fos.write(a);
            fos.flush();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "prueba.txt";
        String texto = "Este seria los bytes del cartel del evento musical con ID 2.";

        try {
            
            FileUtils.write(filePath, texto.getBytes());

           
            byte[] buffer = new byte[1024];
            int bytesLeidos = FileUtils.read(filePath, buffer);
            System.out.println(new String(buffer, 0, bytesLeidos));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
