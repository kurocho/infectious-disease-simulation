package human;

import org.graphstream.graph.Node;

public abstract class Human {

    protected Node node;
    protected Status status;

    public Human() {

    }

    public Human(Human otherHuman) {
        node = otherHuman.node;
        status = otherHuman.status;
        setStatus();
    }

    public Human(Node node) {
        this.node = node;
        setStatus();
    }

    public boolean isInfected(){
        return false;
    };

    public boolean isImmune(){
        return false;
    };

    public boolean isSusceptible(){
        return false;
    };


    public void setNode(Node n) {
        node = n;
        setStatus();
    }

    private void setStatus() {
        setStyle();
    }

    protected abstract void setStyle();

    public Node getNode() {
        return node;
    }
}
