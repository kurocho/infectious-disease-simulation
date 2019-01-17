package com.agh.miss;

import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSourceDGS;

import java.io.IOException;

public class Video {
    public static void main(String args[]) {
        makeVideo();
    }

    private static void makeVideo(){
        FileSinkImages.OutputPolicy outputPolicy = FileSinkImages.OutputPolicy.BY_EVENT;

        String prefix = "simulation/";
        FileSinkImages.OutputType type = FileSinkImages.OutputType.PNG;
        FileSinkImages.Resolution resolution = FileSinkImages.Resolutions.HD1080;
        FileSinkImages fsi = new FileSinkImages(
                prefix, type, resolution, outputPolicy );
        fsi.setQuality(FileSinkImages.Quality.HIGH);


        fsi.setStyleSheet(
                "graph { padding: 10px; fill-color: #2b2b2b; }" +
                        "node { size: 10px; fill-color: #ffc438; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "node.infected_lethal { fill-color: #000000; } " +
                        "node.infected_moderate { fill-color: #ff0000; } " +
                        "node.infected_mild { fill-color: #800080; } " +
                        "node.susceptible { fill-color: #ffc438; } " +
                        "node.immune { fill-color: #00ff00; } " +
                        "edge { size: 0.1px; fill-color: #bbb; text-color: #bbb; text-alignment: under; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 0px, 5px; }" +
                        "edge.loop { text-alignment: left; text-background-mode: rounded-box; text-background-color: #565656; text-padding: 5px, 4px; text-offset: 20px, -25px; }");

        fsi.setOutputPolicy( outputPolicy );
        fsi.setLayoutPolicy( FileSinkImages.LayoutPolicy.COMPUTED_ONCE_AT_NEW_IMAGE );

        FileSourceDGS dgs = new FileSourceDGS();
        dgs.addSink( fsi );

        try {
            fsi.begin(prefix);
            dgs.begin( "graph.txt" );
            while(dgs.nextStep());
            dgs.end();
            fsi.end();

        } catch (IOException e) {
            e.printStackTrace();
        }





    }
}
