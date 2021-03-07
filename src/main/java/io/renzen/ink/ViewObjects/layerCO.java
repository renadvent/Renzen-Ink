package io.renzen.ink.ViewObjects;

import io.renzen.ink.ViewPanels.RenderLayers.AbstractCustomRenderLayer;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Data
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class layerCO {

    List<BufferedImage> toolBuffers;
    List<BufferedImage> strokeBuffers;
    BufferedImage baseBuffer;

    //TODO working on. Should add RenderLayer here? instead of in CanvasPanel
    List<CasterCO> casterCOList = new ArrayList<>();
    ArrayList<AbstractCustomRenderLayer> renderLayerArrayList = new ArrayList<>();



}
