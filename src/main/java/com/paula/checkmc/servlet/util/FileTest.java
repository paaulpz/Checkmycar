/**
 * 
 */
package com.paula.checkmc.servlet.util;

import java.io.File;

/**
 * 
 */
public class FileTest {



	public static void main(String[] args) {
	 
	
/*	File f = new File("Macintosh HD:/t/hola.txt");
	try {
		  f.createNewFile();
	}catch(IOException ioe) {
		ioe.printStackTrace();
	}
  
	f.deleteOnExit();
	
	*/
	
		
	File f3 =  new File ("Macintosh HD");
	
	
	String[] lista = f3.list();

	if (lista != null) {
	    for (int i = 0; i < lista.length; i++) {
	        System.out.println(lista[i]);
	    }
	}
	
    f3.le
	
	/*
	FileInputStream fis; 
	
	FileOutputStream fos; 
	*/
}

}