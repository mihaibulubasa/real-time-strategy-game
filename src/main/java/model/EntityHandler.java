package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityHandler {
    private Map<Long, Army> armies;

    public EntityHandler() {
        armies = new HashMap<>();
    }


    public Army getArmy(long id) {
        return armies.get(id);
    }

    public void addArmy(Army army) {
        if(army != null) {
            armies.put(army.getId(), army);
        }
    }

    public boolean removeArmy(long id) {
        Army removed = armies.remove(id);
        return removed != null;
    }

    public void killArmy(long id) {
        if(removeArmy(id)) {
            IdGenerator.getInstance().destroyId(id);
        }
    }

    public Collection<Army> getArmies() {
        return armies.values();
    }

    public void tick() {
        for(Army army : armies.values()) {
            army.tick();
        }
    }
}
