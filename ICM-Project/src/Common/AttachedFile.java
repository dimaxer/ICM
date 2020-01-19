package Common;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class represents an Attached File.
 */
public class AttachedFile implements Serializable
{
	public static final String DEFAULT_SAVE_PATH = ".\\src\\server\\RequestAttachments\\";
	private static final long serialVersionUID = 1L;
	private String fileName = "";
	private String extension = "";
	private File file;
	
	/** Constructs an attached file\
	 * 
	 * @param file file
	 */
	public AttachedFile(File file) {
		setFile(file);
		initProperties();
	}
	
	/** Constructs an attached file
	 * 
	 * @param filePath path
	 */
	public AttachedFile(String filePath) {
		this(new File(filePath));
	}
	
	/**
	 * Copy this file into a different path, wherever this instance might be.
	 * @param path Path to copy the file to. Input Example: ".\\src\\server\\RequestAttachments"
	 */
	public void copy(String path) {
	  	try {
		  	// Files
		    File outputFile = new File(path + fileName + extension);
		    
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
	
	/**
	 * Copy this file into a different path, wherever this instance might be.
	 * uses default path to copy attachments to
	 */
	public void copy() {
		copy(DEFAULT_SAVE_PATH);
	}
	
	/**
	 * This method opens the attached file on desktop if possible
	 * @throws IOException exception
	 */
	public void open() throws IOException {
        // Check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }
        
        Desktop.getDesktop().open(file);
	}
	
	/** sets the file
	 * 
	 * @param file file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/** gets the file
	 * 
	 * @return file
	 */
	public File getFile() {
		return this.file;
	}

	/** gets file name
	 * 
	 * @return file name
	 */
	public String getFileName() {
		return fileName;
	}

	/** sets file name
	 * 
	 * @param fileName file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/** gets extension
	 * 
	 * @return extension
	 */
	public String getExtension() {
		return extension;
	}

	/** sets extension
	 * 
	 * @param extension extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * This utility method returns the first occurence of File if it exists in DEFAULT_SAVE_PATH 
	 * @param fileName File Name
	 * @return File if exists, null otherwise
	 */
	public static File getFileAttachmentByName(String fileName) {
	    File folder = new File(DEFAULT_SAVE_PATH);
	    File[] listOfFiles = folder.listFiles();

	    for (File file : listOfFiles)
	        if (file.isFile()) {
	            String[] filename = file.getName().split("\\.(?=[^\\.]+$)"); //split filename from it's extension
	            if(filename[0].equalsIgnoreCase(fileName)) //matching defined filename
	                return new File(DEFAULT_SAVE_PATH + filename[0] + "." + filename[1]); // match occured.
	        }
		return null;
	}
	
	private void initProperties() {
		fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1) return;
		extension = fileName.substring(dotIndex);
		fileName = fileName.substring(0, dotIndex);
	}
}
