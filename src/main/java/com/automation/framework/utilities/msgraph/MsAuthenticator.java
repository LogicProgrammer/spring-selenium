package com.automation.framework.utilities.msgraph;

import com.automation.framework.utilities.ConfigUtils;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * For authenticating to graph api, make sure you have register application in azure active directory
 * this class needs following information
 *  1. tenant id
 *  2. client id
 *  3. client secret  
 * required information will be fetched from test-data.properties file
 */
public class MsAuthenticator {

    private final ConfigUtils config = new ConfigUtils();

    public TokenCredentialAuthProvider clientCredentials() {
        config.load("src/main/resources/test-data.yaml");
        String tenantID = config.getDecodedValue("graph.tenant_id");
        String clientID = config.getDecodedValue("graph.client_id");
        String clientSecret = config.getDecodedValue("graph.client_secret");
        return getClientCredentialsAuthenticator(tenantID, clientID, clientSecret);
    }

    public TokenCredentialAuthProvider getClientCredentialsAuthenticator(String tenantID, String clientID, String clientSecret) {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(clientID)
                .clientSecret(clientSecret)
                .tenantId(tenantID)
                .build();
        List<String> scopes = new ArrayList<>() {{
            add("https://graph.microsoft.com/.default");
        }};
        return new TokenCredentialAuthProvider(scopes, credential);
    }


}
