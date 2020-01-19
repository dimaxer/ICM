package LogicController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Common.MyFile;
import Utilities.MessageObject;
import Utilities.RequestType;

/** This is the logical controller of view attached files */
public class ViewAttachedFilesController extends BaseController {

	private String savePath;

	/**
	 * this method sends to the server a list of file names and the requests id and
	 * asks that the server will sends the requested files back
	 * 
	 * @param fileNames names
	 * @param requestID id
	 */
	public void askTheServerToDownloadFiles(String[] fileNames, String requestID) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add((Object) fileNames);
		list.add(requestID);
		MessageObject message = new MessageObject(RequestType.DownloadAttachedFiles, list);

		sendMessage(message);
	}

	/**
	 * this method asks the server to send the requests attached file names
	 * 
	 * @param requestID id
	 */
	public void showAttachedFiles(String requestID) {

		ArrayList<Object> list = new ArrayList<Object>();
		list.add(requestID);
		MessageObject message = new MessageObject(RequestType.ViewAttachedFiles, list);
		sendMessage(message);
	}

	/**
	 * this method download to the path that was selected all the files that were
	 * selected
	 * 
	 * @param message data
	 */
	public void handleDownloadAttachedFiles(MessageObject message) {
		MyFile[] downloadFileList = (MyFile[]) message.getArgs().get(0);

		for (MyFile file : downloadFileList) {
			dwonloadFile(savePath, file);

		}

	}
	/**
	 * save the path of the file
	 * @param savePath the path of the file
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	/**
	 * get the savePath
	 * @param savePath path 
	 * @return the save path
	 */
	public String getSavePath(String savePath) {
		return savePath;
	}
}
