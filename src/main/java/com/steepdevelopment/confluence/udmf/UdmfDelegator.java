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
import com.atlassian.confluence.pages.Comment;
import com.atlassian.confluence.pages.CommentManager;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.user.User;

public class UdmfDelegator implements UdmfInterface {

	Logger log = Logger.getLogger(this.getClass());
	PageManager pageManager;
	
	private CommentManager commentManager;
	private ContentEntityManager contentEntityManager;
	
	private UserAccessor userAccessor;
	public static final String DATE_FORMAT = "yyyy:MM:dd:HH:mm:ss:SS"; //XXX settable?
	
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

	public String setCreateDate(String token, String date, String id) throws RemoteException {
		log.debug("set Create Date method: passed date = " + date + " id = " + id);
		if (this.contentEntityManager == null) {
			String msg = "Cannot set created date. ContentEntityManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
		if (ceo == null) {
			String msg = "Cannot set last modifier. No content entity exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		Date formattedDate;
		try {
			formattedDate = createDate(date);
		} catch (ParseException e1) {
			String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
			log.error(msg);
			throw new RemoteException(msg);
		}
		SaveContext context = new DefaultSaveContext();
		pageManager.saveContentEntity(ceo, context);
		ceo.setCreationDate(formattedDate);
		return formatDate(ceo.getCreationDate());
		
		/*Page page = pageManager.getPage(Long.parseLong(id));
		if (page == null) {
			String msg = "Cannot set created date. No page exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		Date formattedDate;
		try {
			formattedDate = createDate(date);
		} catch (ParseException e1) {
			String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
			log.error(msg); //XXX Settable?
			throw new RemoteException(msg);
		}
		//Note: We do not clone the page because that will create a new item in the page history
		SaveContext context = new DefaultSaveContext();
		pageManager.saveContentEntity(page, context);
		//HACK ALERT: We set this data after the save.
		page.setCreationDate(formattedDate);
		return formatDate(page.getCreationDate());*/
	}
	
	public String setCreator(String token, String username, String id) throws RemoteException {
		return setCreator(token, username, id, true); //default usersMustExist is true
	}
	
	public String setCreator(String token, String username, String id, boolean usersMustExist) 
			throws RemoteException {
		log.debug("set Creator method: passed username = " + username + " id = " + id);
		if (this.contentEntityManager == null) {
			String msg = "Cannot set creator. ContentEntityManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
		if (ceo == null) {
			String msg = "Cannot set last modifier. No content entity exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		if (usersMustExist) {
			User user = userAccessor.getUser(username);
			if (user == null) throw new RemoteException("Cannot set creator. User does not exist: " + username);
		}
		SaveContext context = new DefaultSaveContext();
		context.setUpdateLastModifier(true);
		contentEntityManager.saveContentEntity(ceo, context);
		ceo.setCreatorName(username);
		return ceo.getCreatorName();
		
		/*
		Page page = pageManager.getPage(Long.parseLong(id));
		if (page == null) {
			String msg = "Cannot set creator. No page exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		if (usersMustExist) {
			User user = userAccessor.getUser(username);
			if (user == null) throw new RemoteException("Cannot set creator. User does not exist: " + username);
		}
		//Note: We do not clone the page because that will create a new item in the page history//		}
		SaveContext context = new DefaultSaveContext();
		context.setUpdateLastModifier(true);
		pageManager.saveContentEntity(page, context);
		//HACK ALERT: We set this data after the save.
		page.setCreatorName(username);
		return page.getCreatorName();
		*/
	}
	
	public String setLastModifiedDate(String token, String date, String id) throws RemoteException {
		log.debug("set Last Modified Date method: passed date = " + date + " id = " + id);
		if (this.contentEntityManager == null) {
			String msg = "Cannot set last modified date. ContentEntityManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
		if (ceo == null) {
			String msg = "Cannot set last modifier. No content entity exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		Date formattedDate;
		try {
			formattedDate = createDate(date);
		} catch (ParseException e1) {
			String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
			log.error(msg);
			throw new RemoteException(msg);
		}
		SaveContext context = new DefaultSaveContext();
		pageManager.saveContentEntity(ceo, context);
		ceo.setLastModificationDate(formattedDate);
		return formatDate(ceo.getLastModificationDate());
		
		/*Page page = pageManager.getPage(Long.parseLong(id));
		if (page == null) {
			String msg = "Cannot set last modified date. No page exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		Date formattedDate;
		try {
			formattedDate = createDate(date);
		} catch (ParseException e1) {
			String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
			log.error(msg); //XXX Settable?
			throw new RemoteException(msg);
		}
		SaveContext context = new DefaultSaveContext();
		//Note: We do not clone the page because that will create a new item in the page history//
		pageManager.saveContentEntity(page, context);
		//HACK ALERT: We set this data after the save.
		page.setLastModificationDate(formattedDate);
		return formatDate(page.getLastModificationDate());*/
	}
	
	public String setLastModifier(String token, String username, String id) throws RemoteException {
		return setLastModifier(token, username, id, true); //default usersMustExist is true
	}
	
	public String setLastModifier(String token, String username, String id, boolean usersMustExist) 
		throws RemoteException {
		log.debug("set Last Modifier method: passed username = " + username + " id = " + id);
		if (this.contentEntityManager == null) {
			String msg = "Cannot set last modifier. ContentEntityManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		ContentEntityObject ceo = contentEntityManager.getById(Long.parseLong(id));
		if (ceo == null) {
			String msg = "Cannot set last modifier. No content entity exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		if (usersMustExist) {
			User user = userAccessor.getUser(username);
			if (user == null) throw new RemoteException("Cannot set creator. User does not exist: " + username);
		}
		SaveContext context = new DefaultSaveContext();
		context.setUpdateLastModifier(true);
		contentEntityManager.saveContentEntity(ceo, context);
		ceo.setLastModifierName(username);
		return ceo.getLastModifierName();
		
		/*
		Page page = pageManager.getPage(Long.parseLong(id));
		if (page == null) {
			String msg = "Cannot set last modifier. No page exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		if (usersMustExist) {
			User user = userAccessor.getUser(username);
			if (user == null) throw new RemoteException("Cannot set creator. User does not exist: " + username);
		}
		//Note: We do not clone the page because that will create a new item in the page history
//		Page orig = null;
//		try {
//			orig = (Page) page.clone();
//		} catch (CloneNotSupportedException e) {
//			log.error("Couldn't clone page.");
//			throw new RemoteException(e);
//		}
		SaveContext context = new DefaultSaveContext();
		context.setUpdateLastModifier(true);
		//Note: We do not clone the page because that will create a new item in the page history
//		pageManager.saveContentEntity(page, orig, context);
		pageManager.saveContentEntity(page, context);
		//HACK ALERT: If we set the modifier name before saving the page, it'll
		//force the username to be the logged in user. But setting the username
		//after the save allows the username to be something else. This seems pretty dodgy, 
		//especially across Confluences
		page.setLastModifierName(username);
		return page.getLastModifierName();
		*/
	}
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}
	
	public void setUserAccessor(UserAccessor userAccessor) {
		this.userAccessor = userAccessor;
	}
	
	public void setCommentManager(CommentManager commentManager) {
		this.commentManager = commentManager;
	}

	public void setContentEntityManager(ContentEntityManager contentEntityManager) {
		this.contentEntityManager = contentEntityManager;
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

	public String setCreatorForComment(String token, String username, String id) throws RemoteException {
		if (this.commentManager == null) {
			String msg = "Cannot set creator. CommentManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		Comment comment = commentManager.getComment(Long.parseLong(id));
		if(comment != null) {
			comment.setCreatorName(username);
			return comment.getCreatorName();
		} else {
			String msg = "Cannot set creator. No comment exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
	}
	
	public String setCreateDateForComment(String token, String date, String id) throws RemoteException {
		log.debug("set Create Date For Comment method: passed date = " + date + " id = " + id);
		if (this.commentManager == null) {
			String msg = "Cannot set created date. CommentManager is null.";
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		Comment comment = commentManager.getComment(Long.parseLong(id));
		
		if (comment == null) {
			String msg = "Cannot set created date. No comment exists for id: " + id;
			log.error(msg);
			throw new RemoteException(msg);
		}
		Date formattedDate;
		try {
			formattedDate = createDate(date);
		} catch (ParseException e1) {
			String msg = "Date was not in expected format. Date: " + date + " Format: " + DATE_FORMAT;
			log.error(msg);
			throw new RemoteException(msg);
		}
		
		comment.setCreationDate(formattedDate);
		return formatDate(comment.getCreationDate());
	}
}
