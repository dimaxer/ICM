package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class represents an Attached File.
 * @author Raz Malka
 */
public class AttachedFile implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String fileName = "";
	private String extension = "";
	private File file;
	
	public AttachedFile(String filePath) {
		setFile(new File(filePath));
		initProperties();
	}
	
	/**
	 * Copy this file into the a different path, wherever this instance might be.
	 * @param path Path to copy the file to. Input Example: "server\\RequestAttachments"
	 */
	public void copy(String path) {
	  	try {
		  	// Files
		    File outputFile = new File(".\\src\\" + path + "\\" + fileName + extension);
		    
		    // Streams
		    FileInputStream fis = new FileInputStream(getFile());
		    FileOutputStream fos = new FileOutputStream(outputFile);

		    // Write New File
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = fis.read(buffer)) > 0)
		        fos.write(buffer, 0, length);
		    
		    // Close Streams
		    fis.close(); fos.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	private void initProperties() {
		fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		extension = fileName.substring(dotIndex);
		fileName = fileName.substring(0, dotIndex);
	}
}
