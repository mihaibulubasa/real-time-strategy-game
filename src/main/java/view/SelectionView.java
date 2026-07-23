package view;

import java.util.ArrayList;
import java.util.List;

public class SelectionView {
    private ArmyView root;
    private List<ArmyView> selected;

    public SelectionView() {
        selected = new ArrayList<>();
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
                relative.setHover(true);
            }
        }
    }

    public List<ArmyView> getSelected() {
        return selected;
    }
}
