package de.jodabyte.springsecuritylabs.authorizationserver;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationServerE2ETests {

    private final String REDIRECT_URI = "http://localhost:9000/ping";
    private final String CLIENT_ID = "public-client";
    private final String CLIENT_SECRET = "backtothefuture2015";
    private final String AUTHORIZATION_REQUEST = UriComponentsBuilder
            .fromPath("/oauth2/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", CLIENT_ID)
            .queryParam("scope", "openid")
            .queryParam("redirect_uri", REDIRECT_URI)
            .queryParam("code_challenge", "Ysfc9fVQd2SrxVmfR50fD7gerq8OcWtFKB65Na2mACo")
            .queryParam("code_challenge_method", "S256")
            .toUriString();
    private final String TOKEN_REQUEST = new StringBuilder()
            .append("&grant_type=authorization_code")
            .append("&redirect_uri=" + REDIRECT_URI)
            .append("&code_verifier=b314d939e19e1b9460381810ce6afe52549c922bcd6b")
            .toString();

    @Autowired
    private WebClient webClient;

    @Autowired
    private MockMvc mockMvc;

    private <P extends Page> P signIn(HtmlPage page, String username, String password) throws IOException {
        HtmlInput usernameInput = page.querySelector("input[name=\"username\"]");
        HtmlInput passwordInput = page.querySelector("input[name=\"password\"]");
        HtmlButton signInButton = page.querySelector("button");

        usernameInput.type(username);
        passwordInput.type(password);
        return signInButton.click();
    }

    private void assertLoginPage(HtmlPage page) {
        assertThat(page.getUrl().toString()).endsWith("/login");

        HtmlInput usernameInput = page.querySelector("input[name=\"username\"]");
        HtmlInput passwordInput = page.querySelector("input[name=\"password\"]");
        HtmlButton signInButton = page.querySelector("button");

        assertThat(usernameInput).isNotNull();
        assertThat(passwordInput).isNotNull();
        assertThat(signInButton.getTextContent()).isEqualTo("Sign in");
    }

    private void assertAuthorizationCodeExists(WebResponse response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
        String location = response.getResponseHeaderValue("location");
        assertThat(location).startsWith(REDIRECT_URI);
        assertThat(location).contains("code=");
    }

    private String createUriStringForTokenRequest(WebResponse response) {
        String location = response.getResponseHeaderValue("location");
        String code = StringUtils.substringAfter(location, "?code=");
        return TOKEN_REQUEST + "&code=" + code;
    }

    private void assertTokenExists(MvcResult result) throws UnsupportedEncodingException {
        MockHttpServletResponse response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(
                "access_token", "scope", "token_type", "expires_in"
        );
    }

    @Test
    @DisplayName("Given valid credentials" +
            "When requesting access to a protected resource using authorization code flow" +
            "Then respond with the protected resource.")
    void testGrandTypeAuthorizationCode() throws Exception {
        // Log in
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.webClient.getOptions().setRedirectEnabled(false);
        this.webClient.getCookieManager().clearCookies();
        signIn(this.webClient.getPage("/login"), "r2d2", "legend1");

        // Request authorization code
        WebResponse responseWithCode = this.webClient.getPage(AUTHORIZATION_REQUEST).getWebResponse();
        assertAuthorizationCodeExists(responseWithCode);

        // Get Token
        MvcResult responseWithToken = this.mockMvc.perform(post("/oauth2/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(createUriStringForTokenRequest(responseWithCode))
                        .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andReturn();
        assertTokenExists(responseWithToken);
    }

    @Test
    @DisplayName("Given valid credentials" +
            "When requesting access to a protected resource using client credentials flow" +
            "Then respond with the protected resource.")
    void testGrandTypeClientCredentials() throws Exception {
        String requestBody = "grant_type=client_credentials&scope=openid";

        MvcResult responseWithToken = this.mockMvc.perform(post("/oauth2/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(requestBody)
                        .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andReturn();

        assertTokenExists(responseWithToken);
    }

}
