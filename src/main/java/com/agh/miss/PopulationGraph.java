package com.agh.miss;

import com.agh.miss.human.Human;
import com.agh.miss.human.Immune;
import com.agh.miss.human.Infected;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;
import java.util.function.Consumer;

public class PopulationGraph extends SingleGraph {

    Random random = new Random();

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
        n.addAttribute("Human", h);
    }

    void changeHumanState(Node n, Human h) {
        bindHumanToNode(n, h);
    }

    Human getHumanFromNode(int nodeId) {
        return this.getNode(nodeId).getAttribute("Human");
    }

    Human getHumanFromNode(Node n) {
        return n.getAttribute("Human");
    }

    void addHuman(Human human, int maxLinkPerStep) {
        int graphNodeCount = getNodeCount();
        String newId = graphNodeCount + "";
        Node newNode = this.addNode(newId);
        human.setNode(newNode);

        int targetDegree = random.nextInt(maxLinkPerStep) + 1;
        targetDegree = Math.min(targetDegree, graphNodeCount);

        int edgesCreated = 0;
        if (getNodeCount() > 1) {
            // shuffle list to make random (but "preferenced" ) connections
            List<Node> nodes = new ArrayList<Node>(getNodeSet());
            Collections.shuffle(nodes);
            // dont stop until node will get at least one edge
            while (newNode.getDegree() == 0) {
                for (Node currentNode : nodes) {
                    if (!currentNode.equals(newNode) && !currentNode.hasEdgeBetween(newNode)) {
                        double relativeDegree = ((double) currentNode.getDegree() / getDegreeSum());
                        if (Math.random() <= relativeDegree) {
                            connectNodes(newNode, currentNode);
                            edgesCreated++;
                        }
                        if (edgesCreated >= targetDegree) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void simulateCures() {
        int counter = 0;
        for (Node currentNode : getNodeSet()) {
            Human human = currentNode.getAttribute("Human");
            if (human.isInfected()) {
                Infected infected = (Infected) human;
                double curabilty = infected.getDiseaseStrain().getCurability();
                if (100 * Math.random() < curabilty) {
                    changeHumanState(currentNode, new Immune());
                    counter += 1;
                }
            }
        }
        System.out.println(counter + " people were cured.");
    }

    public void simulateInfections() {
        List<Node> nodes = new ArrayList<Node>(getNodeSet());
        Collections.shuffle(nodes);
        int counter = 0;
        for (Node currentNode : nodes) {
            Human human = currentNode.getAttribute("Human");
            if (human.isInfected()) {
                Infected infected = (Infected) human;
                double infectiousness = infected.getDiseaseStrain().getInfectiounsness();
                Iterator<Node> iterator = currentNode.getNeighborNodeIterator();
                if(iterator.hasNext()){
                    Node neighbourNode = iterator.next();
                    Human neighbourHuman = neighbourNode.getAttribute("Human");
                    if (neighbourHuman.isSusceptible() && 100 * Math.random() < infectiousness) {
                        changeHumanState(neighbourNode,
                                new Infected(neighbourHuman, infected.getStrainType(), infected.getDiseaseStrain()));
                        counter += 1;
                    }
                }
            }
        }
        System.out.println(counter + " poeple were infected.");
    }

    public void simulateDeaths() {
        int counter = 0;
        for (Node currentNode : getNodeSet()) {
            Human human = currentNode.getAttribute("Human");
            if (human.isInfected()) {
                Infected infected = (Infected) human;
                double mortality = infected.getDiseaseStrain().getMortality();
                if (100 * Math.random() < mortality) {
                    removeNode(currentNode);
                    counter += 1;
                }
            }
        }
        System.out.println(counter + " poeple died.");
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


    void killRandomHuman() {
        removeNode(getRandomNode());
    }

    void killHuman(Human human) {
        this.removeNode(human.getNode());
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
}
