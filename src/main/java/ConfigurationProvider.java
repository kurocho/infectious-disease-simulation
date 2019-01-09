import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProvider {
    private static ConfigurationProvider configurationProvider = new ConfigurationProvider();

    private Properties properties;
    private int nodeCount;
    private int maxLinksPerStep;
    private double baselineInfectedPercentage;
    private double baselineImmunePercentage;

    private ConfigurationProvider() {
        this.properties = new Properties();
        try (InputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
            nodeCount = Integer.parseInt(properties.getProperty("node_count"));
            maxLinksPerStep = Integer.parseInt(properties.getProperty("max_links_per_step"));
            baselineInfectedPercentage = Double.parseDouble(properties.getProperty("baseline_infected_percentage"));
            baselineImmunePercentage = Double.parseDouble(properties.getProperty("baseline_immune_percentage"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ConfigurationProvider getInstance() {
        return configurationProvider;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getMaxLinksPerStep() {
        return maxLinksPerStep;
    }

    public double getBaselineInfectedPercentage() {
        return baselineInfectedPercentage;
    }

    public double getBaselineImmunePercentage() {
        return baselineImmunePercentage;
    }
}
