package io.renzen.ink.CommandObjectsPanel;

import io.renzen.ink.CommandObjectsDomain.CasterCO;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Data
public class CanvasPanelCO {

    List<BufferedImage> toolBuffers;
    List<BufferedImage> strokeBuffers;
    BufferedImage baseBuffer;

    List<CasterCO> casterCOList = new ArrayList<>();

}
