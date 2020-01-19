package Common;

import java.io.Serializable;
/** This class represents an a file that would be attached */
public class MyFile implements Serializable {
	
	private String Description=null;
	private String fileName=null;	
	private int size=0;
	public  byte[] mybytearray;
	
	/** Initializes the byte array
	 * 
	 * @param size size
	 */
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	/** Constructor of MyFile
	 * 
	 * @param fileName name
	 */
	public MyFile( String fileName) {
		this.fileName = fileName;
	}
	
	/** get file name
	 * 
	 * @return name
	 */
	public String getFileName() {
		return fileName;
	}

	/** set file name
	 * 
	 * @param fileName name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/** get size
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/** set size
	 * 
	 * @param size size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/** get byte array
	 * 
	 * @return byte array
	 */
	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	/** get certain index of byte array
	 * 
	 * @param i index
	 * @return byte
	 */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	/** set byte array
	 * 
	 * @param mybytearray byte array
	 */
	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	/** get description
	 * 
	 * @return description
	 */
	public String getDescription() {
		return Description;
	}

	/** set description
	 * 
	 * @param description description
	 */
	public void setDescription(String description) {
		Description = description;
	}	
}

