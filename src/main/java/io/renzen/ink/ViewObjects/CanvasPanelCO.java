package io.renzen.ink.ViewObjects;

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
