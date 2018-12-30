import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PopulationGraph extends SingleGraph {

    int numberOfCreatedNodes = 0;
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

    void bindNodeWithHuman(Node n, Human h) {
        n.addAttribute("Human", h);
    }

    void changeHumanState(Node n, Human h) {
        n.setAttribute("Human", h);
    }

    Human getHumanFromNode(int count) {
        return this.getNode(count).getAttribute("Human");
    }

    Human getHumanFromNode(Node n) {
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
                    double p = (double) n.getDegree() / getDegreeSum();
                    if (Math.random() <= p) {
                        connectNodes(newNode, n);
                        linkCount++;
                    }
                    if(linkCount >= maxLinkPerStep)
                        break;
                }
            }
        }
    }

    Node getRandomNode(){
        return getNode(random.nextInt(getNodeCount()));
    }

    void connectNodes(Node first, Node second){
        if(!first.hasEdgeBetween(second))
            addEdge(first.getId() + "_" + second.getId(), first, second);
    }

    int getDegreeSum(){
        return getNodeSet().parallelStream().mapToInt(Node::getDegree).sum();
    }


    void killRandomHuman(){
        removeNode(getRandomNode());
    }
    void killHuman(Human human) {
        this.removeNode(human.node);
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
