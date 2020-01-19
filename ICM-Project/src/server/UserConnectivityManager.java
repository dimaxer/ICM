package server;


import java.io.IOException;
import java.util.HashMap;

import ocsf.server.ConnectionToClient;

/**
 * a class the manages all logged in user's
 */
public class UserConnectivityManager {

	private static UserConnectivityManager singletonInstance = null;
	private HashMap<String, ConnectionToClient> loggedOnUsers = new HashMap<String, ConnectionToClient>();

	private UserConnectivityManager() {

		singletonInstance = this;

	}

	/**
	 * This is the constuctor of the class.
	 * @return User Connectivity Manager
	 */
	public static UserConnectivityManager getInstance() {
		if (singletonInstance == null)
			singletonInstance = new UserConnectivityManager();
		return singletonInstance;
	}

	/**
	 * this method adds a user to the logged on hash map of users only if he's not
	 * logged on already
	 * 
	 * @param userID id
	 * @param client client
	 * @return was successful
	 */
	public boolean addLoggedOnUser(String userID, ConnectionToClient client) {
		if (checkIfThisUserIsAllreadyLoggedOn(userID, client))
			return false;
		loggedOnUsers.put(userID, client);
		return true;
	}

	/**
	 * method that checks if a user is logged on or off or crashed and returns true
	 * if he's logged on and false if he logged off or crashed
	 * 
	 * @param userID id
	 * @param client client
	 * @return was successful
	 */
	private boolean checkIfThisUserIsAllreadyLoggedOn(String userID, ConnectionToClient client) {
		if (loggedOnUsers.containsKey(userID)) {
			try {
				loggedOnUsers.get(userID).sendToClient("check if alive");
			} catch (IOException e) {
				try {
					loggedOnUsers.get(userID).close();
					loggedOnUsers.remove(userID);

					return false;
				} catch (IOException e1) {
					System.out.println("Thread is is busy with i/o action, still alive?");
					e1.printStackTrace();
				}

			}
			return true;
		}
		return false;
	}

	/**
	 * a method that deleted a user from the hash map if he pressed log out
	 * 
	 * @param userID id
	 * @return was successful
	 */
	public boolean logOutUser(String userID) {
		if (loggedOnUsers.containsKey(userID)) {
			loggedOnUsers.remove(userID);
			return true;
		}
		return false;
	}
}
