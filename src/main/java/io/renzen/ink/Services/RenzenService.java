package io.renzen.ink.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.Converters.ProfileJSONToActionPanelInfoCO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
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

    final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    final String root = "https://renzen.io/";

//    public uploadImageToRenzen(){
//
//        //BSON binary
//
//        Binary binaryImage;
//
//    }

    Optional<ActionPanelAccountInfoCO> userInfo;

    public ActionPanelAccountInfoCO getLoggedInUser() {
        return userInfo.orElse(null);
    }

    public ActionPanelAccountInfoCO getLoginInfo(String username, String password) {

        var request = webClient
                .post()

                //.uri(URI.create("localhost:8080/login"))
                .uri(URI.create("https://renzen.io/login"))
                .body(Mono.just(new UserNamePassword(username, password)), UserNamePassword.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);

        ObjectMapper mapper = new ObjectMapper();

        var jacksonResponse = Objects.requireNonNull(request.exchange().block()).
                bodyToMono(String.class).block();

        ProfileJSONToActionPanelInfoCO profileJSONToActionPanelInfoCO = new ProfileJSONToActionPanelInfoCO();

        try {
            JsonNode actualObj = mapper.readTree(jacksonResponse);
            System.out.println("loaded profile for " + actualObj.path("name").asText());
            userInfo = (Optional.of(profileJSONToActionPanelInfoCO.toActionPanelProfileInfoCO(actualObj)));
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

    @Getter
    @Setter
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
