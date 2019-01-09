package human;

import org.graphstream.graph.Node;

public class Immune extends Human {

    public Immune() {
    }

    public Immune(Human human) {
        super(human);
    }

    public Immune(Node node) {
        super(node);
    }

    @Override
    protected void setStyle() {
        node.setAttribute("ui.class", "immune");
    }

    @Override
    public boolean isImmune() {
        return true;
    }
}
