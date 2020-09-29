package io.renzen.ink.CommandObjectsDomain;

import io.renzen.ink.DomainObjects.Caster;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class CasterCO extends Caster {

    BufferedImage strokeBuffer;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
//        if (this == o) return true;
//        if (!(o instanceof CasterCO)) return false;
//        if (!super.equals(o)) return false;
//
//        CasterCO casterCO = (CasterCO) o;
//
//        return name != null ? name.equals(casterCO.name) : casterCO.name == null;
//
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


}
