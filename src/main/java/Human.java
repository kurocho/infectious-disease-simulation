import org.graphstream.graph.Node;

public abstract class Human{

    Node node;

    public Human(Human human) {
        node = human.node;
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
