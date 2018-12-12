import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MissApp {
    Graph baGraph;

    public void start() {
        initBAGraph(1000, 2);
        setStyle();
        setInfectedNodes(0.5);

    }

    void initBAGraph(Integer numberOfNodes, Integer maxNumberOfLinksBetweenNodes) {

        baGraph = new SingleGraph("Barabàsi-Albert");
        Generator gen = new BarabasiAlbertGenerator(maxNumberOfLinksBetweenNodes);
        // Generate nodes:
        gen.addSink(baGraph);
        gen.begin();
        for (int i = 0; i < numberOfNodes; i++) {
            gen.nextEvents();
        }
        gen.end();;
        Viewer viewer = new Viewer(baGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
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

    void setStyle() {
        baGraph.addAttribute("ui.quality");
        baGraph.addAttribute("ui.antialias");
        baGraph.addAttribute("ui.stylesheet",
                "graph { padding: 100px; fill-color: #2b2b2b; }" +
                        "node { size: 5px; fill-color: #ffc438; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.infected { fill-color: #ff0000; } " +
                        "node.susceptible { fill-color: #ffc438; } " +
                        "node.recovered { fill-color: #00ff00; } " +
                        "edge { fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }");
    }

    void setInfectedNodes(double percent){
        for (int i =0;i<baGraph.getNodeCount()*percent/100;i++) {
            baGraph.getNode((int) Math.floor(Math.random()*baGraph.getNodeCount())).setAttribute("ui.class", "infected");
        }
    }
}
