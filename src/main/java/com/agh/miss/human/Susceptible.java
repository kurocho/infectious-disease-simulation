package com.agh.miss.human;

import org.graphstream.graph.Node;

public class Susceptible extends Human {


    public Susceptible() {
    }

    public Susceptible(Human human) {
        super(human);
        setStyle();
    }

    public Susceptible(Node node) {
        super(node);
        setStyle();
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
