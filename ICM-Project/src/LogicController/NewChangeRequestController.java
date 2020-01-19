package LogicController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Common.MyFile;
import Utilities.MessageObject;
import Utilities.RequestType;
import client.Client;
import javafx.event.ActionEvent;

/** This is the logical controller of new change request */
public class NewChangeRequestController extends BaseController {
	/**
	 * This method handles the event where submit was pressed logically.
	 * @param args arguments
	 */
	public void submitWasPressed(ArrayList<Object> args) {
		MessageObject msg = new MessageObject(RequestType.NewChangeRequest, args);
		sendMessage(msg);
	}
/**
 * this function sends a file to the server
 * to be attached to a certain request
 * @param file file
 * @param requestID id
 */
	public void sendFileToServer(File file,String requestID) {
		MyFile newFile = new MyFile(file.getName());
		String localFilePath = file.getAbsolutePath();

		byte[] myByteArray = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

			newFile.initArray(myByteArray.length);
			newFile.setSize(myByteArray.length);

			bufferedInputStream.read(newFile.getMybytearray(), 0, myByteArray.length);

			
			ArrayList<Object> list = new ArrayList<Object>();
			list.add((Object) newFile);
			list.add((Object)requestID);
			MessageObject message = new MessageObject(RequestType.AttachFile, list);

			sendMessage(message);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
