import org.graphstream.graph.Node;

public class Susceptible extends Human {

    public Susceptible(Human human) {
        super(human);
    }

    public Susceptible(Node node) {
        super(node);
    }

    @Override
    void setStyle() {
        node.setAttribute("ui.class", "susceptible");
    }

    @Override
    boolean isInfected() {
        return false;
    }
}
