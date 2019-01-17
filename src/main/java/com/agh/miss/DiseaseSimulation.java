package com.agh.miss;

import com.agh.miss.disease.DiseaseStrainProvider;
import com.agh.miss.disease.StrainType;
import com.agh.miss.human.Human;
import com.agh.miss.human.Immune;
import com.agh.miss.human.Infected;
import com.agh.miss.human.Susceptible;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.measure.ChartMeasure;
import org.graphstream.algorithm.measure.ChartSeries2DMeasure;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class DiseaseSimulation {
    private ConfigurationProvider configurationProvider;
    private DiseaseStrainProvider diseaseStrainProvider;

    private int nodeCount;
    private int maxLinksPerStep;
    private static final PopulationGraph graph = new PopulationGraph("Barabàsi-Albert");
    private static final FileSinkDGS dgs = new FileSinkDGS();

    DiseaseSimulation() {
        configurationProvider = ConfigurationProvider.getInstance();
        diseaseStrainProvider = DiseaseStrainProvider.getInstance();
        nodeCount = configurationProvider.getNodeCount();
        maxLinksPerStep = configurationProvider.getMaxLinksPerStep();
    }

    void start() {
        setStyle();
        setZoom();
        initFileSave();
        initBAGraph();
        initializeImmuneHuman(configurationProvider.getBaselineImmunePercentage());
        initializeInfectedHuman(configurationProvider.getBaselineInfectedPercentage());
        runSimluation(configurationProvider.getSimulationRunTime());
        finishFileSave();
    }

    private void initBAGraph() {
        Generator generator = new BarabasiAlbertGenerator(maxLinksPerStep);
        generator.addSink(graph);
        generator.begin();
        for (Node node : graph.getNodeSet()) {
            Human human = new Susceptible(node);
            graph.bindHumanToNode(node, human);
        }
        int count = graph.getNodeCount();
        for (int i = count; i < nodeCount+count; i++) {
            generator.nextEvents();
            Node node = graph.getNode(i);
            Human human = new Susceptible(node);
            graph.bindHumanToNode(node, human);
        }
        generator.end();
        graph.numberOfCreatedNodes = graph.getNodeCount();
        graph.stats.setBorn(graph.numberOfCreatedNodes);
    }

    private void setStyle() {
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet",
                "graph { padding: 10px; fill-color: #2b2b2b; }" +
                        "node { size: 5px; fill-color: #ffc438; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.infected_lethal { fill-color: #000000; } " +
                        "node.infected_moderate { fill-color: #ff0000; } " +
                        "node.infected_mild { fill-color: #800080; } " +
                        "node.susceptible { fill-color: #ffc438; } " +
                        "node.immune { fill-color: #00ff00; } " +
                        "edge { size: 0px; fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
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

    private void initializeInfectedHuman(double baselinePercentage) {
        graph.getNodeSet().stream()
                .filter(n -> ((Human) n.getAttribute("Human")).isSusceptible())
                .limit((int) Math.floor(graph.getNodeCount() * (baselinePercentage / 100)))
                .forEach(n -> {
                    Human h = n.getAttribute("Human");
                    StrainType strainType = diseaseStrainProvider.getRandomStrain();
                    graph.changeHumanState(h.getNode(),
                            new Infected(h, strainType, diseaseStrainProvider.getStrain(strainType)));
                });
    }

    private void initializeImmuneHuman(double baselinePercentage) {
        graph.getNodeSet().stream()
                .filter(n -> ((Human) n.getAttribute("Human")).isSusceptible())
                .limit((int) Math.floor(graph.getNodeCount() * (baselinePercentage / 100)))
                .forEach(n -> {
                    Human h = n.getAttribute("Human");
                    graph.changeHumanState(h.getNode(), new Immune(h));
                });
    }

    private void runSimluation(int simulationRunTime) {
        double deathsPerDay, birthsPerDay;
        ChartSeries2DMeasure inf = new ChartSeries2DMeasure("infected");
        ChartSeries2DMeasure cur = new ChartSeries2DMeasure("cured");
        ChartSeries2DMeasure die = new ChartSeries2DMeasure("died");
        ChartSeries2DMeasure bor = new ChartSeries2DMeasure("born");
        int size = 500;
        inf.setWindowSize(size);
        cur.setWindowSize(size);
        die.setWindowSize(size);
        bor.setWindowSize(size);
        int day = 1;

        while (simulationRunTime-- >= 0) {
            sleep(1000);
            deathsPerDay = configurationProvider.getDeathRate() * graph.getNodeCount() / 1000 / 365;
            birthsPerDay = configurationProvider.getBirthRate() * graph.getNodeCount() / 1000 / 365;
            graph.simulateInfections(configurationProvider.getMaxNumberOfMeetings());
            graph.simulateCures();
            graph.simulateDeaths();
            if (Math.random() <= deathsPerDay)
                graph.killRandomHuman();
            if (Math.random() <= birthsPerDay)
                graph.addHuman(new Susceptible(), configurationProvider.getMaxLinksPerStep());


            inf.addValue(day, graph.getInfectedAmount());
            cur.addValue(day, graph.stats.getCuredCounter());
            bor.addValue(day, graph.stats.getBirthCounter());
            die.addValue(day, graph.stats.getDiseaseDeathCounter()+graph.stats.getNaturalDeathCounter());
            day++;

            System.out.println("Simulation time: " + simulationRunTime + "; node count: " + graph.getNodeCount());
            System.out.println("Infected at this moment: " + graph.getInfectedAmount() + "\n");
            graph.stats.summary();

            if (graph.getInfectedAmount() == 0)
                break;
        }
        ChartMeasure.PlotParameters params = new ChartMeasure.PlotParameters();
        params.xAxisLabel = "Days";
        params.yAxisLabel = "Humans";


        try {
            params.title = "Infected chart";
            inf.plot(params);
            params.title = "Cured chart";
            cur.plot(params);
            params.title = "Died chart";
            die.plot(params);
            params.title = "Born chart";
            bor.plot(params);
        } catch (ChartMeasure.PlotException e) {
            e.printStackTrace();
        }

    }


    private void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void initFileSave(){
        try {
            PrintWriter w = new PrintWriter("graph.txt");
            graph.addSink(dgs);
            dgs.begin(w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finishFileSave(){
        try {
            dgs.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
