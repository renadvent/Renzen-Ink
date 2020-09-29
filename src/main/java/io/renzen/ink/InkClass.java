package io.renzen.ink;

import io.renzen.ink.Views.ActionPanel;
import io.renzen.ink.Views.CanvasPanel;
import io.renzen.ink.Views.Menu;
import io.renzen.ink.Views.StrokePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class InkClass {

    private static final long serialVersionUID = 1L;

    public final CanvasPanel canvasPanel;
    public final ActionPanel actionPanel;
    public final StrokePanel strokePanel;

    @Autowired
    public InkClass(CanvasPanel canvasPanel, ActionPanel actionPanel, StrokePanel strokePanel) {
        this.canvasPanel = canvasPanel;
        this.actionPanel = actionPanel;
        this.strokePanel = null;
    }
}
