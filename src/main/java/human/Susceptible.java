package human;

import org.graphstream.graph.Node;

public class Susceptible extends Human {


    public Susceptible() {
    }

    public Susceptible(Human human) {
        super(human);
    }

    public Susceptible(Node node) {
        super(node);
    }

    @Override
    protected void setStyle() {
        node.setAttribute("ui.class", "susceptible");
    }

    @Override
    public boolean isSusceptible() {
        return true;
    }
}
