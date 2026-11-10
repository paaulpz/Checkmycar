package com.paula.checkmc.servlet.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletDiskFileUpload;

import com.paula.checkmc.model.ContentException;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {

	public static byte[] getBytes(HttpServletRequest request, String fileParameterName) throws ContentException {
		byte[] bytes = null;
		try {
		// Recuperamos de la request los bytes del fichero asociado al input foto-peril
		if (!JakartaServletDiskFileUpload.isMultipartContent(request)) {
            return null;
        }

        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

		JakartaServletDiskFileUpload upload = new JakartaServletDiskFileUpload(factory);

		upload.setMaxFileSize(5L * 1024 * 1024); // 5 MB por archivo
		upload.setMaxSize(6L * 1024 * 1024); // 6 MB por petición

		
		List<DiskFileItem> items = upload.parseRequest(request);
		
		try {
			for (DiskFileItem item : items) {
				if (!item.isFormField() && fileParameterName.equals(item.getFieldName())) {

					if (item.getName() == null || item.getName().isEmpty() || item.getSize() == 0) {
						return null;
					}

					// Aquí tienes todo el contenido del fichero:
					bytes = item.getInputStream().readAllBytes();

				}
			}
		} finally {
			// Elimina los archivos temporales creados por la librería.
			for (DiskFileItem item : items) {
				item.delete();
			}
		}
		} catch (IOException fue) {
			throw new ContentException(fileParameterName, fue);
		}

		return bytes;
	}

}
