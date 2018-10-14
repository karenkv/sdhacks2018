/*
import com.docusign.esign.api.*;
import com.docusign.esign.client.*;
import com.docusign.esign.model.*;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import java.awt.Desktop;

import java.io.IOException;

import org.json.*;

public class DocuSignExample {
  public static void main(String[] args) {
    JSONObject obj = new JSONObject("credentials.json");
    String RedirectURI = obj.getJSONObject("Docusign_Java").getString("REDIRECT_URI");
    String ClientSecret =  obj.getJSONObject("Docusign_Java").getString("CLIENT_SECRET");
    String IntegratorKey = obj.getJSONObject("Docusign_Java").getString("INTEGRATOR_KEY");
    String BaseUrl = "https://demo.docusign.net/restapi";

    ApiClient apiClient = new ApiClient(BaseUrl);
    try
    {
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        // STEP 1: AUTHENTICATE TO RETRIEVE ACCOUNTID & BASEURL
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        String randomState = "*^.$DGj*)+}Jk";
        java.util.List<String> scopes = new java.util.ArrayList<String>();
        scopes.add(OAuth.Scope_SIGNATURE);
        // get DocuSign OAuth authorization url
        URI oauthLoginUrl = apiClient.getAuthorizationUri(IntegratorKey, scopes, RedirectURI, OAuth.CODE, randomState);
        // open DocuSign OAuth login in the browser
		    Desktop.getDesktop().browse(oauthLoginUrl);
        // IMPORTANT: after the login, DocuSign will send back a fresh
        // authorization code as a query param of the redirect URI.
        // You should set up a route that handles the redirect call to get
        // that code and pass it to token endpoint as shown in the next
        // lines:
        /*String code = "[once_you_get_the_oauth_code_put_it_here]";
        OAuth.OAuthToken oAuthToken = apiClient.generateAccessToken(IntegratorKey, ClientSecret, code);

        System.out.println("OAuthToken: " + oAuthToken);

        // now that the API client has an OAuth token, let's use it in all
        // DocuSign APIs
        UserInfo userInfo = apiClient.getUserInfo(oAuthToken.getAccessToken());

        System.out.println("UserInfo: " + userInfo);
        // parse first account's baseUrl
        // below code required for production, no effect in demo (same
        // domain)
        apiClient.setBasePath(userInfo.getAccounts().get(0).getBaseUri() + "/restapi");
        Configuration.setDefaultApiClient(apiClient);
		String accountId = userInfo.getAccounts().get(0).getAccountId();*/
		/*
          /////////////////////////////////////////////////////////////////////////////////////////////////////////
          // *** STEP 2: CREATE ENVELOPE FROM TEMPLATE
          /////////////////////////////////////////////////////////////////////////////////////////////////////////

          // create a new envelope to manage the signature request
          EnvelopeDefinition envDef = new EnvelopeDefinition();
          envDef.setEmailSubject("Docusign Photo Release Form");

          // assign template information including ID and role(s)
          envDef.setTemplateId(obj.getJSONObject("Docusign_Java").getString("TEMPLATE_ID"));

          // create a template role with a valid templateId and roleName and assign signer info
          TemplateRole tRole = new TemplateRole();
          tRole.setRoleName("Signer");
          tRole.setName(getName());
          tRole.setEmail(getEmail());

          // create a list of template roles and add our newly created role
          java.util.List<TemplateRole> templateRolesList = new java.util.ArrayList<TemplateRole>();
          templateRolesList.add(tRole);

          // assign template role(s) to the envelope
          envDef.setTemplateRoles(templateRolesList);

          // send the envelope by setting |status| to "sent". To save as a draft set to "created"
          envDef.setStatus("sent");

          // instantiate a new EnvelopesApi object
          EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

          // call the createEnvelope() API
          EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
        }
        catch (ApiException ex)
        {
          System.out.println("Exception: " + ex);
        }
        catch (Exception e)
        {
          System.out.println("Exception: " + e.getLocalizedMessage());
        }
  }

  public String getName(){
    return name;
  }

  public String getEmail(){
    return email;
  }

  public void setName(String name){
      this.name = name;
  }

  public void setEmail(String email){
      this.email = email;
  }
}
*/