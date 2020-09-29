package io.renzen.ink.Services;

import io.renzen.ink.CommandObjectsDomain.CasterCO;
import io.renzen.ink.DomainObjects.Caster;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class CasterService {

    //int counter=0;
    final ArrayList<Caster> casterArrayList = new ArrayList<>();

    Caster selectedCaster;

    //TODO use this to store rendered Casters that haven't changed
    List<CasterCO> casterRenderCache = new ArrayList<>();


    public Optional<CasterCO> findInCache(String id) {
        for (var casterCO : casterRenderCache) {
            if (casterCO.getName().equals(id)) {
                return Optional.of(casterCO);
            }
        }
        return Optional.empty();
    }


    public void selectCaster(String id) {
        selectedCaster = findByName(id);
    }

    public Caster getSelectedCaster() {
        return selectedCaster;
    }

    public Caster save(Caster caster) {

        casterArrayList.add(caster);

//        counter++;
//        caster.setName("caster " + counter);

        return caster;

    }

    public Caster findByName(String name) {
        for (Caster caster : casterArrayList) {
            if (caster.getName().equals(name)) {
                return caster;
            }
        }

        return null;
    }


    public List<Caster> getAll() {
        return casterArrayList;
    }
}
