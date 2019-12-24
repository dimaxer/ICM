package LogicController;

import Gui.ClientUI;
import client.Client;;

public class ConnectServerManualyController extends BaseController {

	private ClientUI clientUI;

	public ClientUI getClientUI() {
		return clientUI;
	}

	public void setClientUI(ClientUI clientUI) {
		this.clientUI = clientUI;
	}

	public ConnectServerManualyController(Client client, ClientUI clientUI) {
		super(client);
		this.clientUI = clientUI;

	}

	public Boolean setClient(String ip) {
		return clientUI.setClient(ip);
	}

}
