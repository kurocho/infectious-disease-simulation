package com.agh.miss.human;

import org.graphstream.graph.Node;

public abstract class Human {

    Node node;
    private Status status;

    Human() {

    }

    Human(Human otherHuman) {
        setNode(otherHuman.node);
        status = otherHuman.status;
        setStatus();
    }

    public Human(Node node) {
        this.node = node;
        setStatus();
    }

    public boolean isInfected() {
        return false;
    }

    public boolean isImmune() {
        return false;
    }

    public boolean isSusceptible() {
        return false;
    }


    public void setNode(Node n) {
        node = n;
        n.setAttribute("Human", this);
    }

    private void setStatus() {

    }

    protected abstract void setStyle();

    public Node getNode() {
        return node;
    }
}
