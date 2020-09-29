package io.renzen.ink;

import io.renzen.ink.Views.ActionPanel;
import io.renzen.ink.Views.CanvasPanel;
import org.springframework.stereotype.Component;

@Component
public class InkClass {

    private static final long serialVersionUID = 1L;

    public final CanvasPanel canvasPanel;
    public final ActionPanel actionPanel;


    public InkClass(CanvasPanel canvasPanel, ActionPanel actionPanel) {
        this.canvasPanel = canvasPanel;
        this.actionPanel = actionPanel;
    }
}
