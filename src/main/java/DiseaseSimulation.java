import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class DiseaseSimulation {
    private int nodeCount = 1000;
    private int nodeLinkCount = 2;
    private PopulationGraph graph;

    void start() {
        initBAGraph();
        setStyle();
        setZoom();
        runSimluation();
    }

    private void initBAGraph() {
        graph = new PopulationGraph("Barabàsi-Albert");
        graph.display(false);
        Generator gen = new BarabasiAlbertGenerator(nodeLinkCount);
        gen.addSink(graph);
        gen.begin();
        for (int i = 0; i < nodeCount; i++) {
            gen.nextEvents();
            Node node = graph.getNode(i);
            Human human = new Susceptible(node);
            graph.bindNodeWithHuman(node, human);
        }
        gen.end();
    }

    private void setStyle() {
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet",
                "graph { padding: 100px; fill-color: #2b2b2b; }" +
                        "node { size: 5px; fill-color: #ffc438; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.infected { fill-color: #ff0000; } " +
                        "node.susceptible { fill-color: #ffc438; } " +
                        "node.immune { fill-color: #00ff00; } " +
                        "edge { fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }");
    }


    private void setZoom(){
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        final ViewPanel view = viewer.addDefaultView(true);
        view.resizeFrame(1000, 800);
        ((ViewPanel) view).addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.consume();
                int i = e.getWheelRotation();
                double factor = Math.pow(1.01, i);
                Camera cam = view.getCamera();
                double zoom = cam.getViewPercent() * factor;
                Point2 pxCenter = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
                Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
                double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu / factor;
                double x = guClicked.x + (pxCenter.x - e.getX()) / newRatioPx2Gu;
                double y = guClicked.y - (pxCenter.y - e.getY()) / newRatioPx2Gu;
                cam.setViewCenter(x, y, 0);
                cam.setViewPercent(zoom);

            }
        });
    }

    private void setInfectedNodes(double percent){
        for (int i = 0; i< graph.getNodeCount()*percent/100; i++) {
            graph.getNode((int) Math.floor(Math.random()* graph.getNodeCount())).setAttribute("ui.class", "infected");
        }
    }

    private void runSimluation(){

    }
}
