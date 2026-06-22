import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseManager extends MouseAdapter {
    private List<MouseDraggedObserver> draggedObservers;
    private List<MouseMovedObserver> movedObservers;
    private List<MouseClickedObserver> clickedObservers;
    private List<MouseRightClicked> rightClickedObservers;
    private List<MouseScrolled> scrolledObservers;
    private Camera camera;
    private Point lastDragPoint;
    private int button;
    private Point mousePoint;


    public MouseManager(Camera camera) {
        this.camera = camera;
        draggedObservers = new ArrayList<>();
        movedObservers = new ArrayList<>();
        rightClickedObservers = new ArrayList<>();
        clickedObservers = new ArrayList<>();
        scrolledObservers = new ArrayList<>();
        mousePoint = new Point();
    }

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        button = e.getButton();
        lastDragPoint = e.getPoint();
    }

    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        mousePoint.x = (int)((e.getPoint().x - camera.getX()) / camera.getZoomFactor());
        mousePoint.y = (int)((e.getPoint().y - camera.getY()) / camera.getZoomFactor());
        if(e.getButton() == MouseEvent.BUTTON1) {
            List<MouseClickedObserver> copy = new ArrayList<>(clickedObservers);
            for (MouseClickedObserver observer : copy) {
                observer.objectClicked(mousePoint.x, mousePoint.y);
            }
        } else if(e.getButton() == MouseEvent.BUTTON3) {
            List<MouseRightClicked> copy = new ArrayList<>(rightClickedObservers);
            for(MouseRightClicked clicked : copy) {
                clicked.rightClick(mousePoint.x, mousePoint.y);
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        for(MouseScrolled observer : scrolledObservers) {
            observer.onMouseScrolled(e.getX(), e.getY(), e.getWheelRotation());
        }
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if(button == MouseEvent.BUTTON3) {
            int yOffset = e.getY() - lastDragPoint.y;
            int xOffset = e.getX() - lastDragPoint.x;
            for(MouseDraggedObserver observer : draggedObservers) {
                observer.onMouseDragged(xOffset, yOffset);
            }
        }
        lastDragPoint = e.getPoint();
    }

    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        mousePoint.x = (int)((e.getX() - camera.getX()) / camera.getZoomFactor());
        mousePoint.y = (int)((e.getY() - camera.getY()) / camera.getZoomFactor());
        List<MouseMovedObserver> copy = new ArrayList<>(movedObservers);
        for(MouseMovedObserver observer : copy) {
            observer.mouseMoved(mousePoint.x, mousePoint.y);
        }
    }

    public void addMouseDraggedObserver(MouseDraggedObserver observer) {
        draggedObservers.add(observer);
    }

    public void addMouseMovedObserver(MouseMovedObserver observer) {
        movedObservers.add(observer);
    }

    public void addMouseClickedObserver(MouseClickedObserver observer) {
        clickedObservers.add(observer);
    }

    public void removeMouseDraggedObserver(MouseDraggedObserver observer) {
        draggedObservers.remove(observer);
    }

    public void removeMouseClickedObserver(MouseClickedObserver observer) {
        clickedObservers.remove(observer);
    }

    public void removeMouseMovedObserver(MouseMovedObserver observer) {
        movedObservers.remove(observer);
    }

    public void addMouseScrolledObserver(MouseScrolled observer) {
        scrolledObservers.add(observer);
    }

    public void removeMouseScrolledObserver(MouseScrolled observer) {
        scrolledObservers.remove(observer);
    }

    public void addRightClickObserver(MouseRightClicked observer) {
        rightClickedObservers.add(observer);
    }
}
