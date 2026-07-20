package model.buildings;

public class Castle extends Building{

    public Castle(String name){
        super(name);
        width = 5;
        height = 5;
    }

    @Override
    public String getTexture() {
        return "CASTLE";
    }
}
