package com.steepdevelopment.confluence.udmf;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

public class UdmfXmlrpcPluginTest extends TestCase {

	Logger log = Logger.getLogger(this.getClass());
	private Properties props;
	private String login;
	private String password;
	private String spaceKey;
	private String url;
	private XmlRpcClient client = null;
	private String loginToken;
	private String author;
	
	private static final String TEST_URL = "test.url";
	private static final String TEST_SPACE = "test.space.key";
	private static final String TEST_USER_LOGIN = "test.user.login";
	private static final String TEST_PASS = "test.user.pass";
	private static final String TEST_AUTHOR = "test.author.username";
	
	public static void main(String[] args) {
		try {
			String date = "2009:02:16:13:29:28:27";
			
			XmlRpcClient client = new XmlRpcClient("http://localhost:8585/confluence/rpc/xmlrpc");
			
			Vector loginParams = new Vector(2);
			loginParams.add("admin");
			loginParams.add("admin");
			String loginToken = (String)client.execute("confluence1.login", loginParams);
			
			Vector paramsVector = new Vector();
			paramsVector.add(loginToken);
			paramsVector.add(date);
			paramsVector.add("8552450");
			
			String api = "udmf";
			String method = "setLastModifiedDate";
			
			String result = (String)client.execute(api + "." + method, paramsVector);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
        super.setUp();
        props = new Properties();
		try {
            props.load(new FileInputStream("test.properties"));
        } catch (IOException e) {
        	e.printStackTrace();
        	fail();
        }
        login = props.getProperty(TEST_USER_LOGIN);
        password = props.getProperty(TEST_PASS);
        spaceKey = props.getProperty(TEST_SPACE);
        url = props.getProperty(TEST_URL);
        author = props.getProperty(TEST_AUTHOR);
	}

	public void testSetCreateDate() throws IOException, XmlRpcException, ParseException {
		XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
		
    	String id = createNewPage();
    	try {
    		String api = "udmf";
    		String method = "setCreateDate";
    		String loginToken = getLoginToken();
    		Vector paramsVector = new Vector();
    		paramsVector.add(loginToken);
    		
    		String date = "1990:01:21:13:29:28:27"; //year:month:date:hour:min:sec:msec
			paramsVector.add(date);
    		paramsVector.add(id);
    		String actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
    		assertEquals(date, actual);
    		
    		//get the page and double check info
    		api = "confluence1";
    		method = "getPage";
    		paramsVector.remove(1); //get rid of the author parameter
    		Hashtable page = (Hashtable) client.execute(api + "." + method, paramsVector);
    		assertNotNull(page);
    		assertEquals(id, page.get("id"));
			Date actualDate = (Date) page.get("created");
			Date expDate = createDate(date);
			assertEquals(getTimestamp(DateFormat.FULL, expDate), 
					getTimestamp(DateFormat.FULL, actualDate));
    	} finally {
    		deleteNewPage(id);
    	}
	}

	public void testSetCreator() throws IOException, XmlRpcException {
		XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
		
    	String id = createNewPage();
    	try {
    		String api = "udmf";
    		String method = "setCreator";
    		String loginToken = getLoginToken();
    		Vector paramsVector = new Vector();
    		paramsVector.add(loginToken);
    		
			paramsVector.add(author);
    		paramsVector.add(id);
    		String actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
    		assertEquals(author, actual);
    		
    		//get the page and double check info
    		api = "confluence1";
    		method = "getPage";
    		paramsVector.remove(1); //get rid of the author parameter
    		Hashtable page = (Hashtable) client.execute(api + "." + method, paramsVector);
    		assertNotNull(page);
    		assertEquals(id, page.get("id"));
    		assertEquals(author, page.get("creator")); 
    	} finally {
    		deleteNewPage(id);
    	}
	}

	public void testSetLastModifiedDate() throws IOException, XmlRpcException, ParseException {
		XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
		
    	String id = createNewPage();
    	try {
    		String api = "udmf";
    		String method = "setLastModifiedDate";
    		String loginToken = getLoginToken();
    		Vector paramsVector = new Vector();
    		paramsVector.add(loginToken);
    		
    		String date = "1999:12:31:23:59:58:57"; //year:month:date:hour:min:sec:msec
			paramsVector.add(date);
    		paramsVector.add(id);
    		String actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
    		assertEquals(date, actual);
    		
    		//get the page and double check info
    		api = "confluence1";
    		method = "getPage";
    		paramsVector.remove(1); //get rid of the author parameter
    		Hashtable page = (Hashtable) client.execute(api + "." + method, paramsVector);
    		assertNotNull(page);
    		assertEquals(id, page.get("id"));
			Date actualDate = (Date) page.get("modified");
			Date expDate = createDate(date);
			assertEquals(getTimestamp(DateFormat.FULL, expDate), 
					getTimestamp(DateFormat.FULL, actualDate));
    	} finally {
    		deleteNewPage(id);
    	}
	}

	public void testSetLastModifier() throws XmlRpcException, IOException {
    	XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
    		
    	String id = createNewPage();
    	try {
    		String api = "udmf";
    		String method = "setLastModifier";
    		String loginToken = getLoginToken();
    		Vector paramsVector = new Vector();
    		paramsVector.add(loginToken);
    		
			paramsVector.add(author);
    		paramsVector.add(id);
    		String actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
    		assertEquals(author, actual);
    		
    		//get the page and double check info
    		api = "confluence1";
    		method = "getPage";
    		paramsVector.remove(1); //get rid of the author parameter
    		Hashtable page = (Hashtable) client.execute(api + "." + method, paramsVector);
    		assertNotNull(page);
    		assertEquals(id, page.get("id"));
    		assertEquals(login, page.get("creator")); //The creator name should be the one used by createNewPage
    		assertEquals(author, page.get("modifier")); //The modifier name should be the one used by setLastModifier
    	} finally {
    		deleteNewPage(id);
    	}

	}
	
	public void testErrorHandling() throws IOException, XmlRpcException {
		String badinput = "foobar" + getUniqueString();
		
		XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
		
    	String id = createNewPage();
    	try {
    		String api = "udmf";
    		String loginToken = getLoginToken();
    		Vector paramsVector = new Vector();
    		paramsVector.add(loginToken);
			paramsVector.add(badinput);
    		paramsVector.add(id);

    		String method = "setLastModifiedDate";
    		try {
    			String actual = (String) client.execute(api  + "." + method, paramsVector);
    			fail();//an exception should be thrown
    		} catch (Exception e) {
    			String msg = e.getMessage();
    			assertTrue(msg.contains("Date was not in expected format"));
    		} 
    		
    		method = "setCreateDate";
    		try {
    			String actual = (String) client.execute(api  + "." + method, paramsVector);
    			fail(); //an exception should be thrown
    		} catch (Exception e) {
    			String msg = e.getMessage();
    			assertTrue(msg.contains("Date was not in expected format"));
    		} 
    		
    		method = "setLastModifier";
    		try {
    			String actual = (String) client.execute(api  + "." + method, paramsVector);
    			fail(); //an exception should be thrown
    		} catch (Exception e) {
    			assertTrue(e.getMessage().contains("User does not exist"));
    		} 
    		
    		method = "setCreator";
    		try {
    			String actual = (String) client.execute(api  + "." + method, paramsVector);
    			fail(); //an exception should be thrown
    		} catch (Exception e) {
    			assertTrue(e.getMessage().contains("User does not exist"));
    		} 

    		boolean usernamesMustExist = false;
			paramsVector.add(new Boolean(usernamesMustExist));
			method = "setLastModifier";
    		String actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
			assertEquals(badinput, actual);
    		
    		method = "setCreator";
    		actual = (String) client.execute(api  + "." + method, paramsVector);
    		assertNotNull(actual);
    		assertEquals(badinput, actual);
    	} finally {
    		deleteNewPage(id);
    	}
	}

	/* helper methods */
	private String getConnectionUrl() {
		return "http://" + url + "/rpc/xmlrpc";
	}


	private XmlRpcClient getXmlrpcClient(String connectionURL) {
		if (client == null) {
			try {
				client = new XmlRpcClient(connectionURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		return client;
	}

	
	private String getLoginToken() throws XmlRpcException, IOException {
		if (loginToken == null) {
			Vector loginParams = new Vector(2);
	        loginParams.add(login);
	        loginParams.add(password);
	        XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
	        loginToken = (String) client.execute("confluence1.login", loginParams);
		}
		return loginToken;
	}

	private String getUniqueString() {
        return String.valueOf((new Date()).getTime());
    }
	
	//gets page id to new page
	private String createNewPage() throws IOException, XmlRpcException {
		String uniqueId = getUniqueString();
		Hashtable page = new Hashtable();
        page.put("title", "Testing Udmf " + uniqueId);
        page.put("content", "foobar " + uniqueId);
        page.put("space", spaceKey);
        
        XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
        String loginToken = getLoginToken();
        Vector paramsVector = new Vector();
        paramsVector.add(loginToken);
        paramsVector.add(page);
        Hashtable resultpage = (Hashtable) client.execute("confluence1.storePage", paramsVector);
        return (String) resultpage.get("id");
	}
	
	//deletes page with given id
	private void deleteNewPage(String id) throws XmlRpcException, IOException {
    	XmlRpcClient client = getXmlrpcClient(getConnectionUrl());
    	String loginToken = getLoginToken();
    	Vector paramsVector = new Vector();
    	paramsVector.add(loginToken);
    	paramsVector.add(id);
    	client.execute("confluence1.removePage", paramsVector);
	}
	
	private Date createDate(String timestamp) throws ParseException {
		return createDate(timestamp, UdmfDelegator.DATE_FORMAT);
	}
	private Date createDate(String timestamp, String format) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(timestamp);
	}
	private String getTimestamp(int format, Date date) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(format, format);
		return (dateFormat.format(date));
	}
}
