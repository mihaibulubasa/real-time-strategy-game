package model;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private Tile location;
    private Tile nextLocation;
    private Tile previousLocation;
    private boolean moving;
    private double movementProgress;
    private double speed;
    private long id;
    private String name;
    private List<Tile> path;

    public Army(String name) {
        this.name = name;
        id = IdGenerator.getInstance().getNewId();
        path = new ArrayList<>();
        speed = 0.05;

    }

    public void tick() {
        if(!moving) {
            return;
        }
        move();
    }

    public void move(){
        if(nextLocation == null) {
            if(!path.isEmpty()) {
                Tile nextStep = path.getFirst();
                if(nextStep.isEmpty()) {
                    nextLocation = path.removeFirst();
                    previousLocation = location;
                    movementProgress = 0;
                } else {
                    moving = false;
                    path.clear();
                    return;
                }
            } else {
                moving = false;
                return;
            }
        }
        movementProgress += speed;
        if(movementProgress >= 0.5 && location != nextLocation) {
            location = nextLocation;
        }
        if(movementProgress >= 1) {
            previousLocation = location;
            nextLocation = null;
            movementProgress = 0;
        }
    }

    public void setLocation(Tile location) {
        this.location = location;
    }

    public void followPath(ArrayList<Tile> path) {
        if(!path.isEmpty()) {
            movementProgress = 0.0;
            moving = true;
            nextLocation = path.removeFirst();
            this.path = path;
        }
    }

    public Tile getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }

    public String getTexture() {
        return "ARMY_WALK";
    }
}
