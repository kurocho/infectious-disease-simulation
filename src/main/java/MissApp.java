import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class MissApp {
    Graph baGraph;
    public void start(){
        initBAGraph(30, 5);
        setGraphAttributes();
    }

    void initBAGraph(Integer numberOfNodes, Integer maxNumberOfLinksBetweenNodes){
        baGraph = new SingleGraph("Barabàsi-Albert");
        // Between 1 and 3 new links per node added.
        Generator gen = new BarabasiAlbertGenerator(maxNumberOfLinksBetweenNodes);
        // Generate 100 nodes:
        gen.addSink(baGraph);
        gen.begin();

        for(int i=0; i<numberOfNodes; i++) {
            gen.nextEvents();
        }

        gen.end();
        baGraph.display();
    }

    void setGraphAttributes(){
        baGraph.addAttribute("ui.quality");
        baGraph.addAttribute("ui.antialias");
        baGraph.addAttribute("ui.stylesheet",
                "graph { padding: 100px; fill-color: #2b2b2b; }" +
                        "node { size: 20px; fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.current { fill-color: #ffc438; }" +
                        "edge { fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }");
    }
}
