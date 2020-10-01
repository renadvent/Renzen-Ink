package io.renzen.ink.Converters;

import com.fasterxml.jackson.databind.JsonNode;
import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileJSONToActionPanelInfoCO {

    public ActionPanelAccountInfoCO toActionPanelProfileInfoCO(JsonNode node) {

        ActionPanelAccountInfoCO infoCO = new ActionPanelAccountInfoCO();
        infoCO.setName(node.path("name").asText());
        infoCO.set_id(node.path("_id").asText());

        //infoCO.setArticles(node.path(""));



        try{
//            var x = node.path("/articleHomePageCOList/_embedded/articleStreamComponentCoes");

            var x = node.at("/articleHomePageCOList/_embedded/articleStreamComponentCoes");

            for (var y : x){



                System.out.println(y.at("/name"));
                var name = y.at("/name").asText();

                var link = y.at("/_links/Tab_Version/href");
                System.out.println(link);

                infoCO.getArticles().add(name, link.asText());
            }


            //infoCO.setArticles((List<JsonNode>) node.path("articleHomePageCOList._embedded.articleStreamComponentCoes"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return infoCO;
    }
}
