import org.graphstream.graph.Node;

public class Infectious extends Human {

    public Infectious() {
    }

    public Infectious(Human human) {
        super(human);
    }

    public Infectious(Node node) {
        super(node);
    }

    @Override
    void setStyle() {
        node.setAttribute("ui.class", "infectious");
    }

    @Override
    boolean isInfected() {
        return true;
    }

}
