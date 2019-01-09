import human.*;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

public class DiseaseSimulation {
    //TODO rozważyć czy nie lepiej zrobić tak że jedna osoba poznaje 1 lub wiecej osob na raz ale tylko się znających
    // lub dodac spotkania tych co znali umarlego?
    private ConfigurationProvider configurationProvider;

    private int nodeCount;
    private int maxLinksPerStep;
    private static final PopulationGraph graph = new PopulationGraph("Barabàsi-Albert");

    DiseaseSimulation() {
        configurationProvider = ConfigurationProvider.getInstance();
        nodeCount = configurationProvider.getNodeCount();
        maxLinksPerStep = configurationProvider.getMaxLinksPerStep();
    }

    void start() {
        setStyle();
        initBAGraph();
        setZoom();
        initializeWithHumanType(configurationProvider.getBaselineInfectedPercentage(), HumanType.INFECTED);
        initializeWithHumanType(configurationProvider.getBaselineImmunePercentage(), HumanType.IMMUNE);
        runSimluation(1000);
    }

    private void initBAGraph() {
        Generator generator = new BarabasiAlbertGenerator(maxLinksPerStep);
        generator.addSink(graph);
        generator.begin();
        for (int i = 0; i < nodeCount; i++) {
            generator.nextEvents();
        }
        for (Node node : graph.getNodeSet()) {
            Human human = new Susceptible(node);
            graph.bindHumanToNode(node, human);
        }
        generator.end();
    }

    private void setStyle() {
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet",
                "graph { padding: 100px; fill-color: #2b2b2b; }" +
                        "node { size: 5px; fill-color: #ffc438; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.infectious { fill-color: #ff0000; } " +
                        "node.susceptible { fill-color: #ffc438; } " +
                        "node.immune { fill-color: #00ff00; } " +
                        "edge { fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }");
    }


    private void setZoom() {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        final ViewPanel view = viewer.addDefaultView(true);
        view.resizeFrame(1000, 800);
        ((ViewPanel) view).addMouseWheelListener(e -> {
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

        });
    }

    private void initializeWithHumanType(double baselinePercentage, HumanType humanType) {
        int count = graph.getNodeCount();
        int howManyToAffect = (int) Math.floor(count * (baselinePercentage / 100));
        while (howManyToAffect > 0) {
            int nodeId = (int) Math.floor(Math.random() * count);
            Human human = graph.getHumanFromNode(nodeId);
            switch (humanType) {
                case INFECTED:
                    if (human.isSusceptible()) {
                        graph.changeHumanState(human.getNode(), new Infected(human));
                    }
                    break;
                case IMMUNE:
                    if (human.isSusceptible()) {
                        graph.changeHumanState(human.getNode(), new Immune(human));
                    }
                    break;
            }
            --howManyToAffect;
        }
    }

    private void runSimluation(int dayTime) {
        while (true) {
            sleep(dayTime);
            //born
            graph.addHuman(new Susceptible(), maxLinksPerStep);
//            graph.killRandomHuman();


        }
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
        }
    }
}
