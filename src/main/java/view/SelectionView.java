package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectionView {
    private ArmyView root;
    private List<ArmyView> selected;
    private Map<ArmyView, TileView> mapping;

    public SelectionView() {
        selected = new ArrayList<>();
        mapping = new HashMap<>();
        root = null;
    }

    public void addSelected(ArmyView selected) {
        if(this.selected.isEmpty()) {
            root = selected;
        }
        if(!this.selected.contains(selected)) {
            this.selected.add(selected);
        }
    }

    public void removeSelected(ArmyView selected) {
        this.selected.remove(selected);
        mapping.remove(selected);
        if(this.selected.isEmpty()) {
            root = null;
        } else {
            root = this.selected.get(0);
        }
    }

    public void hoverSelected(TileView tileview, MainPanel panel) {
        for(ArmyView armyview : selected) {
            int i = tileview.getRow() + (armyview.getLocation().getRow() - root.getLocation().getRow());
            int j = tileview.getCol() + (armyview.getLocation().getCol() - root.getLocation().getCol());
            TileView relative = panel.getTileView(i, j);
            if(relative != null) {
                mapping.put(armyview, relative);
                relative.setHover(true);
            }
        }
    }

    public TileView getDestination(ArmyView selectedArmy) {
        return mapping.get(selectedArmy);
    }
}
