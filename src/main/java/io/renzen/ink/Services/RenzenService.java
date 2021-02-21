package io.renzen.ink.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.renzen.ink.Converters.ProfileJSONToActionPanelInfoCO;
import io.renzen.ink.KEYS;
import io.renzen.ink.ViewObjects.ActionPanelAccountInfoCO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Make requests to renzen.io
 * instead of connecting straight to database
 * and to login etc
 */

@Service
@NoArgsConstructor
public class RenzenService {

    final String live = "https://renzen.io";
    final String local = "http://localhost:8080";
    @Getter
    final String root = live;
    final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    @Getter
    @Setter
    String authToken;
    Optional<ActionPanelAccountInfoCO> userInfo;

    public ActionPanelAccountInfoCO getLoggedInUser() {
        return userInfo.orElse(null);
    }

    public ActionPanelAccountInfoCO getLoginInfo(String username, String password) {

        var tokenRequest = webClient
                .post()
                .uri(URI.create(root + "/login"))
                .body(Mono.just(new UserNamePassword(username, password)), UserNamePassword.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);


        ObjectMapper mapper = new ObjectMapper();

        var tokenResponse = Objects.requireNonNull(tokenRequest.exchange().block()).
                bodyToMono(String.class).block();

        System.out.println(tokenResponse);

        //TODO decode JWT

        //TODO REQUEST PROFILE

        ProfileJSONToActionPanelInfoCO profileJSONToActionPanelInfoCO = new ProfileJSONToActionPanelInfoCO();

        try {
            JsonNode actualObj = mapper.readTree(tokenResponse);

            var token = actualObj.path("token");//get token from response
            var stringTest = token.toString();
            var totest = token.toString().substring(8, stringTest.length() - 1);

            setAuthToken(token.toString().substring(1, stringTest.length() - 1));

            //TODO fails here

            var decoded = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEYS.SECRET)).parseClaimsJws(totest).getBody();//set as header

            var id = decoded.get("id");

            System.out.println("about to request profile");

            var profileRequest = webClient
                    .get()
                    .uri(URI.create(root + "/getProfileTabComponentCO/" + id))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header("Authorization: " + token.asText())
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);

            var profileResponse = Objects.requireNonNull(profileRequest.exchange().block()).
                    bodyToMono(String.class).block();

            System.out.println(profileResponse);

            JsonNode actualObj2 = mapper.readTree(profileResponse);

            System.out.println("loaded profile for " + actualObj2.path("name").asText());
            userInfo = (Optional.of(profileJSONToActionPanelInfoCO.toActionPanelProfileInfoCO(actualObj2)));
            return (userInfo.get());//actualObj;
        } catch (Exception e) {
            System.out.println("Couldn't convert login failed" + e.getMessage());
            return null;
        }

    }

    public void viewImageOnWeb(String id) {
        System.out.println("Trying to open");

        try {

            var URI = new java.net.URI(id);
            java.awt.Desktop.getDesktop().browse(URI);

        } catch (Exception e) {
            System.out.println("could not open");
        }
    }

    public void openRenzen() {

        System.out.println("Trying to open");

        try {

            var URI = new java.net.URI(root);
            java.awt.Desktop.getDesktop().browse(URI);

        } catch (Exception e) {
            System.out.println("could not open");
        }

    }


    public HashMap<?, ?> UploadArticle(String fileContent) {
        //create webclient
        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, Object> multiValueMap = new HashMap<>();
        multiValueMap.put("title", "an image");
        multiValueMap.put("file", fileContent);
        multiValueMap.put("userId", getLoggedInUser().get_id());
        //server needs to get userid from auth, not here

        //create request
        var request = webClient
                .post()

                .uri(URI.create(getRoot() + "/CREATE_ARTICLE_DRAFT_FROM_APP"))
                .header("Authorization", getAuthToken())
                .bodyValue(multiValueMap);

        var jacksonResponse = Objects.requireNonNull(request.exchange().block())
                .bodyToMono(HashMap.class).block();

        //print response
        System.out.println(jacksonResponse);
        return jacksonResponse;
    }


    @Data
    @NoArgsConstructor
    static class UserNamePassword {
        String username;
        String password;

        public UserNamePassword(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

}
