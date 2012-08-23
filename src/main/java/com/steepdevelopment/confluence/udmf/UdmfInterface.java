package com.steepdevelopment.confluence.udmf;

import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.SecureRpc;

public interface UdmfInterface extends SecureRpc {

	// login and logout must be required for authentication to be posssible
	public String login(String username, String password) throws RemoteException;
	public boolean logout(String token) throws RemoteException;

	public String setCreator(String token, String username, String id) throws RemoteException;
	public String setLastModifier(String token, String username, String id) throws RemoteException;
	public String setCreator(String token, String username, String id, boolean usersMustExist) throws RemoteException;
	public String setLastModifier(String token, String username, String id, boolean usersMustExist) throws RemoteException;
	public String setCreateDate(String token, String date, String id) throws RemoteException;
	public String setLastModifiedDate(String token, String date, String id) throws RemoteException;
	
}
