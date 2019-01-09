package human;

import org.graphstream.graph.Node;

public class Infected extends Human {

    public Infected() {
    }

    public Infected(Human human) {
        super(human);
    }

    public Infected(Node node) {
        super(node);
    }

    @Override
    protected void setStyle() {
        node.setAttribute("ui.class", "infectious");
    }

    @Override
    public boolean isInfected() {
        return true;
    }

}
