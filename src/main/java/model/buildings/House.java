package model.buildings;

public class House extends Building {

    public House(String name) {
        super(name);
        width = 4;
        height = 7;
    }

    @Override
    public String getTexture() {
        return "HOUSE_IMAGE";
    }
}
