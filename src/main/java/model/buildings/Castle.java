package model.buildings;

public class Castle extends Building{

    public Castle(String name){
        super(name);
        width = 7;
        height = 7;
    }

    @Override
    public String getTexture() {
        return "CASTLE_IMAGE";
    }
}
