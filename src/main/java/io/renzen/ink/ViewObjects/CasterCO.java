package io.renzen.ink.ViewObjects;

import io.renzen.ink.ArtObjects.Caster;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class CasterCO extends Caster {
    BufferedImage strokeBuffer;
}
