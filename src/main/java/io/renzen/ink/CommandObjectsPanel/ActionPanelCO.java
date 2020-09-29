package io.renzen.ink.CommandObjectsPanel;


import io.renzen.ink.DomainObjects.Caster;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ActionPanelCO {

    String selectedCaster;

    List<Caster> casterList = new ArrayList<>();
    int selectedCasterStrikes;
    int selectedCasterTolerance;
    int selectedCasterDetail;

}
