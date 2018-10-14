import com.docusign.esign.api.*;
import com.docusign.esign.client.*;
import com.docusign.esign.client.auth.AccessTokenListener;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;

// import com.docusign.esign.client.auth.OAuth;
// import com.docusign.esign.client.auth.OAuth.UserInfo;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.json.*;

public class DocusignHelper{
	static String name = "Karen Vu";
	static String email = "karenkv@uci.edu";
	
  public static void main(String[] args) {
    JSONObject obj;
    String BaseUrl = "https://demo.docusign.net/restapi";
    String ClientSecret = "";
    String IntegratorKey = "";
    String accountId = "";
    String code = "";
	try {
		obj = new JSONObject(new JSONTokener(new FileReader("credentials.json")));
		ClientSecret =  obj.getJSONObject("Docusign_Java").getString("SECRET_KEY");
	    IntegratorKey = obj.getJSONObject("Docusign_Java").getString("INTEGRATOR_KEY");
	    accountId = obj.getJSONObject("Docusign_Java").getString("ACCOUNT_ID");
	    code = obj.getJSONObject("Docusign_Java").getString("AUTH_CODE");

	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	/*

    ApiClient apiClient = new ApiClient(AuthServerUrl, "docusignAccessCode", IntegratorKey, ClientSecret);
    apiClient.setBasePath(RestApiUrl);
    apiClient.configureAuthorizationFlow(IntegratorKey, ClientSecret, "https://google.com/");
    Configuration.setDefaultApiClient(apiClient);

  	
    try {
    	// get DocuSign OAuth authorization url
    	String oauthLoginUrl = apiClient.getAuthorizationUri();
    	// open DocuSign OAuth login in the browser
    	System.out.println(oauthLoginUrl);
    	Desktop.getDesktop().browse(URI.create(oauthLoginUrl));
    }
    catch (OAuthSystemException ex)
    {
      System.out.println(ex);
    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
 // Java request auth token
    
    // assign it to the token endpoint
    apiClient.getTokenEndPoint().setCode(code);

    // optionally register to get notified when a new token arrives
    apiClient.registerAccessTokenListener(new AccessTokenListener() {
        @Override
        public void notify(BasicOAuthToken token) {
            System.out.println("Got a fresh token: " + token.getAccessToken());
        }
    });

    // following call exchanges the authorization code for an access code and updates 
    // the `Authorization: bearer <token>` header on the api client
    apiClient.updateAccessToken();
    
    com.docusign.esign.client.auth.OAuth.UserInfo user;
	try {
		user = apiClient.getUserInfo(apiClient.getAccessToken());
		apiClient.setBasePath(user.getAccounts().get(0).getBaseUri() + "/restapi");
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ApiException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	 // currently parsing the first account we find in the response
	 */
	
	ApiClient apiClient = new ApiClient(BaseUrl);         
    String randomState = "";
    java.util.List<String> scopes = new java.util.ArrayList<String>();
    scopes.add(OAuth.Scope_SIGNATURE);
    // get DocuSign OAuth authorization url
    /*
    URI oauthLoginUrl = apiClient.getAuthorizationUri(IntegratorKey, scopes, "https://google.com/", OAuth.CODE, randomState);
    // open DocuSign OAuth login in the browser
    try {
		Desktop.getDesktop().browse(oauthLoginUrl);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
    // IMPORTANT: after the login, DocuSign will send back a fresh
    // authorization code as a query param of the redirect URI.
    // You should set up a route that handles the redirect call to get
    // that code and pass it to token endpoint as shown in the next
    // lines:
	try {
		System.out.println(code);
		System.out.println(ClientSecret);
		System.out.println(IntegratorKey);
		OAuth.OAuthToken oAuthToken = apiClient.generateAccessToken(IntegratorKey, ClientSecret, code);
		System.out.println("OAuthToken: " + oAuthToken);
		// now that the API client has an OAuth token, let's use it in all
	    // DocuSign APIs
		
	    com.docusign.esign.client.auth.OAuth.UserInfo userInf = apiClient.getUserInfo(oAuthToken.getAccessToken());

	    System.out.println("UserInfo: " + userInf);
	    // parse first account's baseUrl
	    // below code required for production, no effect in demo (same
	    // domain)
	    apiClient.setBasePath(userInf.getAccounts().get(0).getBaseUri() + "/restapi");
	    Configuration.setDefaultApiClient(apiClient);
		accountId = userInf.getAccounts().get(0).getAccountId();
		Configuration.setDefaultApiClient(apiClient);
	} catch (ApiException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    // create a byte array that will hold our document bytes
    byte[] fileBytes = null;
    String pathToDocument = "/form.pdf";

    try
    {
        String currentDir = System.getProperty("user.dir");

        // read file from a local directory
        Path path = Paths.get(currentDir + pathToDocument);
        fileBytes = Files.readAllBytes(path);
    }
    catch (IOException ioExcp)
    {
        // TODO: handle error
        System.out.println("Exception: " + ioExcp);
        return;
    }

    // create an envelope that will store the document(s), field(s), and recipient(s)
    EnvelopeDefinition envDef = new EnvelopeDefinition();
    envDef.setEmailSubject("Please sign this photo release sent from Photosign");

    // add a document to the envelope
    Document doc = new Document();
    String base64Doc = Base64.getEncoder().encodeToString(fileBytes);
    doc.setDocumentBase64(base64Doc);
    doc.setName("TestFile"); // can be different from actual file name
    doc.setFileExtension(".pdf");
    doc.setDocumentId("1");

    List<Document> docs = new ArrayList<Document>();
    docs.add(doc);
    envDef.setDocuments(docs);

    // add a recipient to sign the document, identified by name and email we used above
    Signer signer = new Signer();
    signer.setEmail(DocusignHelper.getEmail());
    signer.setName(DocusignHelper.getName());
    signer.setRecipientId("1");

    // Must set |clientUserId| to embed the recipient which in turn makes it possible to
    // generate a signing token (link) for them. This is a client defined string value
    // which is also needed to create the recipient view URL in the next step
    signer.setClientUserId("1001");

    // create a |signHere| tab somewhere on the document for the signer to sign
    // default unit of measurement is pixels, can be mms, cms, inches also
    SignHere signHere = new SignHere();
    signHere.setDocumentId("1");
    signHere.setPageNumber("1");
    signHere.setRecipientId("1");
    signHere.setXPosition("100");
    signHere.setYPosition("150");

    // can have multiple tabs, so need to add to envelope as a single element list
    List<SignHere> signHereTabs = new ArrayList<SignHere>();
    signHereTabs.add(signHere);
    Tabs tabs = new Tabs();
    tabs.setSignHereTabs(signHereTabs);
    signer.setTabs(tabs);

    // add recipients (in this case a single signer) to the envelope
    envDef.setRecipients(new Recipients());
    envDef.getRecipients().setSigners(new ArrayList<Signer>());
    envDef.getRecipients().getSigners().add(signer);

    // send the envelope by setting |status| to "sent". To save as a draft set to "created"
    envDef.setStatus("sent");
    System.out.println(envDef);

    try
    {
        // instantiate a new EnvelopesApi object
        EnvelopesApi envelopesApi = new EnvelopesApi();
        // call the createEnvelope() API
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
        System.out.println("EnvelopeSummary: " + envelopeSummary);
        System.out.println(envelopeSummary);
    }
    catch (com.docusign.esign.client.ApiException ex)
    {
        System.out.println("Exception: " + ex);
    }
    
    EnvelopesApi envelopesApi = new EnvelopesApi();

    // set the url where you want the recipient to go once they are done signing
    RecipientViewRequest view = new RecipientViewRequest();
    view.setReturnUrl("https://www.docusign.com");
    view.setAuthenticationMethod("email");

    // recipient information must match embedded recipient info we provided in step #2
    view.setEmail(DocusignHelper.getEmail());
    view.setUserName(DocusignHelper.getName());
    view.setRecipientId("1");
    view.setClientUserId("1001");

    // call the CreateRecipientView API
    ViewUrl recipientView;
	try {
		recipientView = envelopesApi.createRecipientView(accountId, envDef.getEnvelopeId(), view);
		System.out.println("Signing URL = " + recipientView.getUrl());
	    Desktop.getDesktop().browse(URI.create(recipientView.getUrl()));
	} catch (ApiException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
  }

  public static String getName(){
    return name;
  }

  public static String getEmail(){
    return email;
  }

  public static void setName(String modelName){
	  name = modelName;
  }

  public static void setEmail(String modelEmail){
	  email = modelEmail;
  }
}
