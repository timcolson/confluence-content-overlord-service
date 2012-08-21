package com.steepdevelopment.confluence.udmf;

import org.apache.log4j.Logger;

import com.atlassian.confluence.rpc.RemoteException;

public class UdmfXmlrpcPlugin implements UdmfInterface {

	Logger log = Logger.getLogger(this.getClass());
	UdmfInterface delegator;
	public String login(String username, String password)
			throws RemoteException {
		String message = "login should have been intercepted. Can't authenticate.";
		log.error(message);
		throw new RemoteException(message);
	}

	public boolean logout(String token) throws RemoteException {
		String message = "logout should have been intercepted. Can't authenticate.";
		log.error(message);
		throw new RemoteException(message);
	}

	public String setCreateDate(String token, String date, String id)
			throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setCreateDate(token, date, id);
	}

	public String setCreator(String token, String username, String id)
			throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setCreator(token, username, id);
	}

	public String setCreator(String token, String username, String id, boolean usersMustExist) throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setCreator(token, username, id, usersMustExist);
	}

	public String setLastModifiedDate(String token, String date, String id)
			throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setLastModifiedDate(token, date, id);
	}

	public String setLastModifier(String token, String username, String id)
			throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setLastModifier(token, username, id);
	}

	public String setLastModifier(String token, String username, String id, boolean usersMustExist) throws RemoteException {
		//we have to use a delegator because we are trying to access the Confluence API
		return this.delegator.setLastModifier(token, username, id, usersMustExist);
	}

	public void setUdmfDelegator(UdmfInterface udmfDelegator) {
		log.debug("setting delegator");
		this.delegator = udmfDelegator;
	}

	public String setCreatorForComment(String token, String username, String id) throws RemoteException {
		return this.delegator.setCreatorForComment(token, username, id);
	}

	public String setCreateDateForComment(String token, String date, String id) throws RemoteException {
		return this.delegator.setCreateDateForComment(token, date, id);
	}

}
