from __future__ import absolute_import, print_function
from pprint import pprint
import unittest
import webbrowser
from json import load

import docusign_esign as docusign
from docusign_esign import AuthenticationApi, TemplatesApi, EnvelopesApi
from docusign_esign.rest import ApiException

Class DocusignHelper():
    def __init__(self):
        load("credentials.json") as f:
            self._user_name = f["Docusign"]["Username"]
            self._integrator_key = f["Docusign"]["Integrator_Key"]
            self._base_url = "https://demo.docusign.net/restapi"
            self._oauth_base_url = "account.docusign.com"
            self._redirect_uri = "https://www.docusign.com/api"
            self._private_key_filename = "keys/docusign_private_key.txt"
            self._user_id = f["Docusign"]["User_ID"]
            self._template_id = "[TEMPLATE_ID]"
            self._api_client = docusign.ApiClient(base_url)
            self._oauth_login_url = api_client.get_jwt_uri(integrator_key, redirect_uri, oauth_base_url)

webbrowser.open_new_tab(oauth_login_url)
print(oauth_login_url)

api_client.configure_jwt_authorization_flow(private_key_filename, oauth_base_url, integrator_key, user_id, 3600)

docusign.configuration.api_client = api_client

template_role_name = 'Needs to sign'

# create an envelope to be signed
envelope_definition = docusign.EnvelopeDefinition()
envelope_definition.email_subject = 'Please Sign my Python SDK Envelope'
envelope_definition.email_blurb = 'Hello, Please sign my Python SDK Envelope.'

# assign template information including ID and role(s)
envelope_definition.template_id = template_id

# create a template role with a valid template_id and role_name and assign signer info
t_role = docusign.TemplateRole()
t_role.role_name = template_role_name
t_role.name ='Pat Developer'
t_role.email = user_name

# create a list of template roles and add our newly created role
# assign template role(s) to the envelope
envelope_definition.template_roles = [t_role]

# send the envelope by setting |status| to "sent". To save as a draft set to "created"
envelope_definition.status = 'sent'

auth_api = AuthenticationApi()
envelopes_api = EnvelopesApi()

try:
    login_info = auth_api.login(api_password='true', include_account_id_guid='true')
    assert login_info is not None
    assert len(login_info.login_accounts) > 0
    login_accounts = login_info.login_accounts
    assert login_accounts[0].account_id is not None

    base_url, _ = login_accounts[0].base_url.split('/v2')
    api_client.host = base_url
    docusign.configuration.api_client = api_client

    envelope_summary = envelopes_api.create_envelope(login_accounts[0].account_id, envelope_definition=envelope_definition)
    assert envelope_summary is not None
    assert envelope_summary.envelope_id is not None
    assert envelope_summary.status == 'sent'

    print("EnvelopeSummary: ", end="")
    pprint(envelope_summary)

except ApiException as e:
    print("\nException when calling DocuSign API: %s" % e)
    assert e is None # make the test case fail in case of an API exception
