import org.graphstream.graph.Node;

public class Infectious extends Human {

    public Infectious(Node node) {
        super(node);
    }

    @Override
    void setStyle() {
        node.setAttribute("ui.class", "infectious");
    }
}
