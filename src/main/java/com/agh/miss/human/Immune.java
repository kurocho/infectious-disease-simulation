package com.agh.miss.human;

import org.graphstream.graph.Node;

public class Immune extends Human {

    public Immune(Human human) {
        super(human);
        setStyle();
    }

    public Immune(Node node) {
        super(node);
        setStyle();
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
