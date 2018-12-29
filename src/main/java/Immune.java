import org.graphstream.graph.Node;

public class Immune extends Human {

    public Immune(Human human) {
        super(human);
    }

    public Immune(Node node) {
        super(node);
    }

    @Override
    void setStyle() {
        node.setAttribute("ui.class", "immune");
    }

    @Override
    boolean isInfected() {
        return false;
    }

}
