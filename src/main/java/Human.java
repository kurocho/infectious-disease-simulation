import org.graphstream.graph.Node;

public abstract class Human{

    Node node;
    Status status;

    public Human(Human otherHuman) {
        node = otherHuman.node;
        status = otherHuman.status;
        setStatus();
    }

    public Human(Node node) {
        this.node = node;
        setStatus();
    }


    public void setNode(Node n) {
        node = n;
        setStatus();
    }

    public void setStatus(){
        setStyle();
    }

    abstract void setStyle();

    abstract boolean isInfected();
}
