package io.renzen.ink.Converters;

import com.fasterxml.jackson.databind.JsonNode;
import io.renzen.ink.CommandObjectsDomain.ActionPanelAccountInfoCO;
import org.springframework.stereotype.Component;

@Component
public class ProfileJSONToActionPanelInfoCO {

    public ActionPanelAccountInfoCO toActionPanelProfileInfoCO(JsonNode node) {

        ActionPanelAccountInfoCO infoCO = new ActionPanelAccountInfoCO();
        infoCO.setName(node.path("name").asText());
        infoCO.set_id(node.path("_id").asText());

        return infoCO;
    }
}
