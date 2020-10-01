package io.renzen.ink.CommandObjectsDomain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Data
public class ActionPanelAccountInfoCO {
    String name;
    String _id;
    MultiValueMap<String,String> articles = new LinkedMultiValueMap<>();
}
