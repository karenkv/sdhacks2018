#import <DocuSignESign/DSApiClient.h>
#import <DocuSignESign/DSAuthenticationApi.h>
#import <DocuSignESign/DSEnvelopesApi.h>

int main(int argc, char * argv[]) {
    NSString *integratorKey = @"<#INTEGRATOR_KEY#>";
    NSString *userId = @"<#USER_ID#>";
    NSString *oauthBasePath = @"<#OAUTH_BASE_PATH#>";
    NSString *privateKeyFilename = @"<#PRIVATEKEY_FILENAME#>";
    NSInteger jwtTokenExpiresInSeconds = 3600;
    NSString *host = @"https://demo.docusign.net/restapi";

    // instantiate api client, configure environment URL and assign auth data
    DSApiClient* apiClient = [[DSApiClient alloc] initWithBaseURL:[[NSURL alloc] initWithString:host]];
    [apiClient configure_jwt_authorization_flow:integratorKey
                                         userId:userId
                                  oauthBasePath:oauthBasePath
                             privateKeyFilename:privateKeyFilename
                                      expiresIn:jwtTokenExpiresInSeconds
     ];

    XCTestExpectation* expectation = [self expectationWithDescription:@"Get user account id"];

    DSAuthenticationApi *authApi = [[DSAuthenticationApi alloc] initWithApiClient:apiClient];

    // Example of using options
    DSAuthenticationApi_LoginOptions* loginOptions = [[DSAuthenticationApi_LoginOptions alloc] init];
    loginOptions.loginSettings = @"none";
    loginOptions.apiPassword = @"true";
    loginOptions.includeAccountIdGuid = @"true";

    __block NSString *newHost = @"";
    __block NSString *accountId = @"";

    [authApi loginWithApiPassword:loginOptions completionHandler:^(DSLoginInformation *output, NSError *error) {
        if (output != nil && output.loginAccounts != nil && output.loginAccounts.count > 0) {
            DSLoginAccount* loginAccount = [output.loginAccounts objectAtIndex: 0];
            accountId = loginAccount.accountId;
            newHost = [[loginAccount.baseUrl componentsSeparatedByString:@"/v2"] objectAtIndex:0];

            XCTAssertNotNil(loginAccount.accountId);
        } else if(error !=nil) {
            XCTFail(@"%@", error);
        } else {
            XCTFail(@"Unknow error occured. Please try again later.");
        }

        [expectation fulfill];
    }];

    [self waitForExpectationsWithTimeout:1.0 handler:nil];

    expectation = [self expectationWithDescription:@"Send envelope"];

    // Update ApiCLient with the new base url from login call
    apiClient = [[DSApiClient alloc] initWithBaseURL:[[NSURL alloc] initWithString:newHost]];
    [apiClient configure_jwt_authorization_flow:integratorKey
                                         userId:userId
                                  oauthBasePath:oauthBasePath
                             privateKeyFilename:privateKeyFilename
                                      expiresIn:jwtTokenExpiresInSeconds
     ];

    // Create envelope with single document, single signer and one signature tab.
    DSEnvelopeDefinition *envDef = [[DSEnvelopeDefinition alloc] init];
    envDef.emailSubject = @"Please Sign Objc Envelope On Dcoument";
    envDef.emailBlurb = @"Hello, Please sign my Objective-C Envelope";

    DSDocument *doc = [[DSDocument alloc] init];
    doc.name = @"Test.pdf";
    doc.documentId = @"1";

    NSBundle *bundle = [NSBundle bundleForClass:[self class]];
    NSString *path = [bundle pathForResource:@"Test" ofType:@"pdf"];
    NSData *myData = [NSData dataWithContentsOfFile:path];
    doc.documentBase64 = [myData base64EncodedStringWithOptions:0];
    envDef.documents = [NSArray<DSDocument> arrayWithObjects:doc, nil];

    // create a signature tab
    DSSignHere *signHere = [[DSSignHere alloc] init];
    signHere.documentId = @"1";
    signHere.pageNumber = @"1";
    signHere.recipientId = @"1";
    signHere.xPosition = @"100";
    signHere.yPosition = @"100";

    // Add the tab to the signer.
    DSTabs* tabs = [[DSTabs alloc] init];
    tabs.signHereTabs = [NSArray<DSSignHere> arrayWithObjects:signHere, nil];

    // Add a recipient to sign the document
    DSSigner *signer = [[DSSigner alloc] init];
    signer.email = @"<#RECIPIENT_EMAIL#>";
    signer.name = @"<#RECIPIENT_NAME#>";
    signer.recipientId = @"1";

    signer.tabs = tabs;

    NSArray<DSSigner> *signers = [[NSArray<DSSigner> alloc] initWithObjects:signer, nil];

    DSRecipients* recipients = [[DSRecipients alloc] init];
    recipients.signers = signers;

    envDef.recipients = recipients;

    // set status to sent to trigger sending the envelope. Otherwise the envelope will stay in the Drafts folder.
    envDef.status = @"sent";

    // Create and send the envelope
    DSEnvelopesApi *envelopesApi = [[DSEnvelopesApi alloc] initWithApiClient:apiClient];

    DSEnvelopesApi_CreateEnvelopeOptions* createEnvelopeOptions = [[DSEnvelopesApi_CreateEnvelopeOptions alloc] init];
    createEnvelopeOptions.cdseMode = @"true";
    createEnvelopeOptions.mergeRolesOnDraft = @"false";

    [envelopesApi createEnvelopeWithAccountId:accountId envelopeDefinition:envDef options:createEnvelopeOptions completionHandler:^(DSEnvelopeSummary *output, NSError *error) {

        if (output != nil && output.envelopeId != nil) {
            XCTAssertNotNil(output.envelopeId);
        } else if(error !=nil) {
            XCTFail(@"%@", error);
        } else {
            XCTFail(@"Unknow error occured. Please try again later.");
        }

        [expectation fulfill];
    }];

    [self waitForExpectationsWithTimeout:2.0 handler:nil];
}
