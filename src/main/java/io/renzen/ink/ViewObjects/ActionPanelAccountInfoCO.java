package io.renzen.ink.ViewObjects;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class ActionPanelAccountInfoCO {
    String name;
    String _id;
    MultiValueMap<String, String> articles = new LinkedMultiValueMap<>();
}
