package LogicController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Common.MyFile;
import Utilities.MessageObject;
import Utilities.RequestType;

public class ViewAttachedFilesController extends BaseController {

	private String savePath;

	/**
	 * this method sends to the server a list of file names and the requests id and
	 * asks that the server will sends the requested files back
	 * 
	 * @param fileNames
	 * @param requestID
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
	 * @param requestID
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
	 * @param message
	 */
	public void handleDownloadAttachedFiles(MessageObject message) {
		MyFile[] downloadFileList = (MyFile[]) message.getArgs().get(0);

		for (MyFile file : downloadFileList) {
			dwonloadFile(savePath, file);

		}

	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getSavePath(String savePath) {
		return savePath;
	}
}
