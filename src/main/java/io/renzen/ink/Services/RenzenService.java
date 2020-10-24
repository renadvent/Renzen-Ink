package io.renzen.ink.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import io.renzen.ink.Converters.ProfileJSONToActionPanelInfoCO;
import io.renzen.ink.KEYS;
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

import io.jsonwebtoken.*;

import javax.xml.bind.DatatypeConverter;

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

    @Getter@Setter
    String authToken;

    @Getter
    final String root = local;



    final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();



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

        var tokenRequest = webClient
                .post()

                //.uri(URI.create("localhost:8080/login"))
                .uri(URI.create(root+"/login"))
                .body(Mono.just(new UserNamePassword(username, password)), UserNamePassword.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);



        ObjectMapper mapper = new ObjectMapper();

        var tokenResponse = Objects.requireNonNull(tokenRequest.exchange().block()).
                bodyToMono(String.class).block();

        System.out.println(tokenResponse);


        //---------------------------------------------------------------

        //TODO decode JWT

        //TODO REQUEST PROFILE

        ProfileJSONToActionPanelInfoCO profileJSONToActionPanelInfoCO = new ProfileJSONToActionPanelInfoCO();

        try {



            JsonNode actualObj = mapper.readTree(tokenResponse);


            var  token = actualObj.path("token");//get token from response
            var stringTest = token.toString();
            var totest =token.toString().substring(8,stringTest.length()-1);

            setAuthToken(token.toString().substring(1,stringTest.length()-1));

            //TODO fails here

            //Jws<Claims> decoded =null;

            //Jwts.parser().par

            var decoded = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(KEYS.SECRET)).parseClaimsJws(totest).getBody();//set as header



            var id = decoded.get("id");

//            Claims claims = Jwts.parser().parseClaimsJws(token).getBody();
//            String id = (String) claims.get("id");
//
//            var id = decoded;


            var profileRequest =  webClient
                    .get()

                    //.uri(URI.create("localhost:8080/login"))
                    .uri(URI.create(root+"/getProfileTabComponentCO/"+id))
                    //.body(Mono.just(new UserNamePassword(username, password)), UserNamePassword.class)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header("Authorization: "+token.asText())
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
