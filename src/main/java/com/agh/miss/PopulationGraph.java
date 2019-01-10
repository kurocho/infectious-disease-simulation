package com.agh.miss;

import com.agh.miss.human.Human;
import com.agh.miss.human.Immune;
import com.agh.miss.human.Infected;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PopulationGraph extends SingleGraph {

    Random random = new Random();
    int numberOfCreatedNodes = getNodeCount();
    final Stats stats = new Stats();

    public PopulationGraph(String id, boolean strictChecking, boolean autoCreate, int initialNodeCapacity, int initialEdgeCapacity) {
        super(id, strictChecking, autoCreate, initialNodeCapacity, initialEdgeCapacity);
    }

    public PopulationGraph(String id, boolean strictChecking, boolean autoCreate) {
        super(id, strictChecking, autoCreate);
    }

    PopulationGraph(String id) {
        super(id);
    }

    void bindHumanToNode(Node n, Human h) {
        n.setAttribute("Human", h);
    }

    void changeHumanState(Node n, Human h) {
        bindHumanToNode(n, h);
        if(h.isInfected())
            stats.incInfected();
        else if(h.isImmune())
            stats.incCured();

    }


    public static Human getHumanFromNode(Node n) {
        return n.getAttribute("Human");
    }

    void addHuman(Human human, int maxLinkPerStep) {
        maxLinkPerStep = random.nextInt(maxLinkPerStep)+1;
        int linkCount = 0;
        numberOfCreatedNodes++;
        String newId = numberOfCreatedNodes + 1 + "";
        Node newNode = this.addNode(newId);
        human.setNode(newNode);

        if(getNodeCount()>1) {
            // shuffle list to make random (but "preferenced" ) connections
            ArrayList<Node> nodes = new ArrayList<Node>(getNodeSet());
            Collections.shuffle(nodes);
            //dont stop until node will get at least one edge
            while(newNode.getDegree() == 0) {
                for (Node n : nodes) {
                    if(n != newNode && !n.hasEdgeBetween(newNode)) {
                        double p = ((double) n.getDegree() / getDegreeSum());
                        if (Math.random() <= p) {
                            connectNodes(newNode, n);
                            linkCount++;
                        } else if(getDegreeSum() == 0){
                            connectNodes(newNode, getRandomNode());
                            linkCount++;
                        } else if(n.getDegree() == 0){
                            if(Math.random() <= 0.1){
                                connectNodes(newNode, n);
                                linkCount++;
                            }
                        }
                        if (linkCount >= maxLinkPerStep)
                            break;
                    }
                }
            }
        }
        stats.incBirth();
    }

    public void simulateCures() {
        for (Node currentNode : getNodeSet()) {
            Human human = currentNode.getAttribute("Human");
            if (human.isInfected()) {
                Infected infected = (Infected) human;
                double curabilty = infected.getDiseaseStrain().getCurability();
                if (100 * Math.random() < curabilty) {
                    changeHumanState(currentNode, new Immune(currentNode));
                }
            }
        }
    }

    public void simulateInfections(int maxNumberOfMeetings) {
            List<Node> nodes = new ArrayList<Node>(getNodeSet());
            Collections.shuffle(nodes);
            nodes.stream().unordered().
                    filter(n -> getHumanFromNode(n).isInfected()).peek(Node::getEdgeSet).forEach(n ->{
                        Infected infected = (Infected) getHumanFromNode(n);
                        n.getEdgeSet().stream()
                        .limit(maxNumberOfMeetings)
                        .filter(e -> getHumanFromNode(e.getOpposite(n)).isSusceptible()
                                && infected.getDiseaseStrain().getInfectiounsness() > 100 * Math.random())
                        .forEach(e -> changeHumanState(n, new Infected((getHumanFromNode(e.getOpposite(n))),
                                infected.getStrainType(),
                                infected.getDiseaseStrain())));
                    }
            );


    }

    public void simulateDeaths() {
        for (Node currentNode : getNodeSet()) {
            Human human = currentNode.getAttribute("Human");
            if (human.isInfected()) {
                Infected infected = (Infected) human;
                double mortality = infected.getDiseaseStrain().getMortality();
                if (100 * Math.random() < mortality) {
                    removeNode(currentNode);
                    stats.incDiseaseDeaths();
                }
            }
        }
    }

    Node getRandomNode() {
        return getNode(random.nextInt(getNodeCount()));
    }

    void connectNodes(Node first, Node second) {
        if (!first.hasEdgeBetween(second))
            addEdge(first.getId() + "_" + second.getId(), first, second);
    }

    int getDegreeSum() {
        return getNodeSet().parallelStream().mapToInt(Node::getDegree).sum();
    }

    long getInfectedAmount(){
       return  getNodeSet().stream().filter(n->getHumanFromNode(n).isInfected()).count();
    }


    void killRandomHuman() {
        removeNode(getRandomNode());
        stats.incNaturalDeaths();
    }

    void killHuman(Human human) {
        this.removeNode(human.getNode());
        stats.incDiseaseDeaths();
    }

    @Override
    public <T extends Node> T removeNode(Node node) {
        return super.removeNode(node);
    }




    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Actions are performed in the order of iteration, if that
     * order is specified.  Exceptions thrown by the action are relayed to the
     * caller.
     * <p>
     * The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super Node> action) {
        super.forEach(action);
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @implSpec The default implementation creates an
     * <em><a href="../util/Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     * @implNote The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     * @since 1.8
     */
    @Override
    public Spliterator<Node> spliterator() {
        return super.spliterator();
    }


    private synchronized void colorEdge(Edge e){
        e.setAttribute("ui.style","size: 1px; fill-color: red;");
        sleep(100);
        e.setAttribute("ui.style","size: 1px; fill-color: #bbb;");
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
