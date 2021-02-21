package io.renzen.ink.Converters;

import com.fasterxml.jackson.databind.JsonNode;
import io.renzen.ink.ViewObjects.ActionPanelAccountInfoCO;
import org.springframework.stereotype.Component;

@Component
public class ProfileJSONToActionPanelInfoCO {

    public ActionPanelAccountInfoCO toActionPanelProfileInfoCO(JsonNode node) {

        ActionPanelAccountInfoCO infoCO = new ActionPanelAccountInfoCO();
        infoCO.setName(node.path("name").asText());
        infoCO.set_id(node.path("_id").asText());

        try {

            var x = node.at("/articleInfoComponentCOS/_embedded/articleInfoComponentCoes");

            for (var y : x) {


                System.out.println(y.at("/name"));
                var name = y.at("/name").asText();
                var id = y.at("/_id");


                System.out.println(id);

                var link = y.at("/_links/Tab_Version/href");
                System.out.println(link);

                infoCO.getArticles().add(name, id.asText());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoCO;
    }
}
