package com.steepdevelopment.confluence.udmf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.atlassian.confluence.core.ContentEntityManager;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.core.DefaultSaveContext;
import com.atlassian.confluence.core.SaveContext;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.user.User;

public class UdmfDelegator implements UdmfInterface {

	Logger log = Logger.getLogger(this.getClass());
	private final ContentEntityManager contentEntityManager;
	private final UserAccessor userAccessor;
	private final TransactionTemplate transactionTemplate;

	public static final String DATE_FORMAT = "yyyy:MM:dd:HH:mm:ss:SS"; //XXX settable?

	public UdmfDelegator (ContentEntityManager contentEntityManager,
			UserAccessor userAccessor,
			TransactionTemplate transactionTemplate) {
		this.contentEntityManager = contentEntityManager;
		this.userAccessor = userAccessor;
		this.transactionTemplate = transactionTemplate;
	}

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

	public String setCreateDate(String token, final String date, final String id) throws RemoteException {
		try {
			return (String) transactionTemplate.execute(new TransactionCallback()
			{
				public Object doInTransaction()
				{
					log.debug("set Create Date method: passed date = " + date + " id = " + id);
					if (contentEntityManager == null) {
						String msg = "Cannot set created date. ContentEntityManager is null.";
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}

					ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
					if (ceo == null) {
						String msg = "Cannot set last modifier. No content entity exists for id: " + id;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					Date formattedDate;
					try {
						formattedDate = createDate(date);
					} catch (ParseException e1) {
						String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					SaveContext context = newSaveContext();
					contentEntityManager.saveContentEntity(ceo, context);
					ceo.setCreationDate(formattedDate);
					return formatDate(ceo.getCreationDate());
				}

			});
		} catch (IllegalArgumentException e) { 
			//We can't directly throw the RemoteException from within the transaction template, so we 
			//hack it with a RuntimeException (IllegalArgumentException seemed good enough).
			throw new RemoteException(e.getMessage());
		}

	}

	public String setCreator(String token, String username, String id) throws RemoteException {
		return setCreator(token, username, id, true); //default usersMustExist is true
	}

	public String setCreator(String token, final String username, final String id, final boolean usersMustExist) 
			throws RemoteException {
		try {
			return (String) transactionTemplate.execute(new TransactionCallback()
			{
				public Object doInTransaction()
				{
					log.debug("set Creator method: passed username = " + username + " id = " + id);
					if (contentEntityManager == null) {
						String msg = "Cannot set creator. ContentEntityManager is null.";
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}

					ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
					if (ceo == null) {
						String msg = "Cannot set last modifier. No content entity exists for id: " + id;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					if (usersMustExist) {
						User user = userAccessor.getUser(username);
						if (user == null) throw new IllegalArgumentException("Cannot set creator. User does not exist: " + username);
					}
					SaveContext context = newSaveContext();
					context.setUpdateLastModifier(true);
					contentEntityManager.saveContentEntity(ceo, context);
					ceo.setCreatorName(username);
					return ceo.getCreatorName();
				}
			});
		} catch (IllegalArgumentException e) { 
			//We can't directly throw the RemoteException from within the transaction template, so we 
			//hack it with a RuntimeException (IllegalArgumentException seemed good enough).
			throw new RemoteException(e.getMessage());
		}
	}

	public String setLastModifiedDate(String token, final String date, final String id) throws RemoteException {
		try {
			return (String) transactionTemplate.execute(new TransactionCallback()
			{
				public Object doInTransaction()
				{
					log.debug("set Last Modified Date method: passed date = " + date + " id = " + id);
					if (contentEntityManager == null) {
						String msg = "Cannot set last modified date. ContentEntityManager is null.";
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}

					ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
					if (ceo == null) {
						String msg = "Cannot set last modifier. No content entity exists for id: " + id;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					Date formattedDate;
					try {
						formattedDate = createDate(date);
					} catch (ParseException e1) {
						String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					SaveContext context = newSaveContext();
					contentEntityManager.saveContentEntity(ceo, context);
					ceo.setLastModificationDate(formattedDate);
					return formatDate(ceo.getLastModificationDate());
				}
			});
		} catch (IllegalArgumentException e) { 
			//We can't directly throw the RemoteException from within the transaction template, so we 
			//hack it with a RuntimeException (IllegalArgumentException seemed good enough).
			throw new RemoteException(e.getMessage());
		}
	}

	public String setLastModifier(String token, String username, String id) throws RemoteException {
		return setLastModifier(token, username, id, true); //default usersMustExist is true
	}

	public String setLastModifier(String token, final String username, final String id, final boolean usersMustExist) 
			throws RemoteException {
		try {
			return (String) transactionTemplate.execute(new TransactionCallback()
			{
				public Object doInTransaction()
				{
					log.debug("set Last Modifier method: passed username = " + username + " id = " + id);
					if (contentEntityManager == null) {
						String msg = "Cannot set last modifier. ContentEntityManager is null.";
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}

					ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
					if (ceo == null) {
						String msg = "Cannot set last modifier. No content entity exists for id: " + id;
						log.error(msg);
						throw new IllegalArgumentException(msg);
					}
					if (usersMustExist) {
						User user = userAccessor.getUser(username);
						if (user == null) throw new IllegalArgumentException("Cannot set creator. User does not exist: " + username);
					}
					SaveContext context = newSaveContext();
					context.setUpdateLastModifier(true);
					contentEntityManager.saveContentEntity(ceo, context);
					ceo.setLastModifierName(username);
					return ceo.getLastModifierName();
				}
			});
		} catch (IllegalArgumentException e) { 
			//We can't directly throw the RemoteException from within the transaction template, so we 
			//hack it with a RuntimeException (IllegalArgumentException seemed good enough).
			throw new RemoteException(e.getMessage());
		}
	}

	private Date createDate(String timestamp) throws ParseException {
		String format = UdmfDelegator.DATE_FORMAT;  //XXX settable?
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(timestamp);
	}

	private String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT); //XXX settable?
		return (dateFormat.format(date));
	}	

	public DefaultSaveContext newSaveContext() {
		//not a minor edit, don't update last mod, do suppress events
		//these settings help us avoid exceptions
		return new DefaultSaveContext(false,false,true); 
	}
}
