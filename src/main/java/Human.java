import org.graphstream.graph.Node;

public abstract class Human{

    Node node;

    public Human(Node node) {
        this.node = node;
        setStyle();
    }

    public void setNode(Node n) {
        node = n;
        setStyle();
    }

    public Node getNode() {
        return node;
    }


    abstract void setStyle();

}
