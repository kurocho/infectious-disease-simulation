package com.agh.miss;

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
    private int simulationRunTime;

    private double baselineInfectedPercentage;
    private double baselineImmunePercentage;

    private double lethalStrainInfectiousness;
    private double lethalStrainMortality;
    private double moderateStrainInfectiousness;
    private double moderateStrainMortality;
    private double mildStrainInfectiousness;
    private double mildStrainMortality;

    private double lethalStrainBaselinePercentage;
    private double moderateStrainBaselinePercentage;
    private double mildStrainBaselinePercentage;

    private ConfigurationProvider() {
        this.properties = new Properties();
        try (InputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
            nodeCount = Integer.parseInt(properties.getProperty("node_count"));
            maxLinksPerStep = Integer.parseInt(properties.getProperty("max_links_per_step"));
            simulationRunTime = Integer.parseInt(properties.getProperty("simulation_run_time"));

            baselineInfectedPercentage = Double.parseDouble(properties.getProperty("baseline_infected_percentage"));
            baselineImmunePercentage = Double.parseDouble(properties.getProperty("baseline_immune_percentage"));

            lethalStrainInfectiousness = Double.parseDouble(properties.getProperty("lethal_strain_infectiousness"));
            lethalStrainMortality = Double.parseDouble(properties.getProperty("lethal_strain_mortality"));
            moderateStrainInfectiousness = Double.parseDouble(properties.getProperty("moderate_strain_infectiousness"));
            moderateStrainMortality = Double.parseDouble(properties.getProperty("moderate_strain_mortality"));
            mildStrainInfectiousness = Double.parseDouble(properties.getProperty("mild_strain_infectiousness"));
            mildStrainMortality = Double.parseDouble(properties.getProperty("mild_strain_mortality"));

            lethalStrainBaselinePercentage = Double.parseDouble(properties.getProperty("lethal_strain_baseline_percentage"));
            moderateStrainBaselinePercentage = Double.parseDouble(properties.getProperty("moderate_strain_baseline_percentage"));
            mildStrainBaselinePercentage = Double.parseDouble(properties.getProperty("mild_strain_baseline_percentage"));
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

    public int getSimulationRunTime() {
        return simulationRunTime;
    }

    public double getBaselineInfectedPercentage() {
        return baselineInfectedPercentage;
    }

    public double getBaselineImmunePercentage() {
        return baselineImmunePercentage;
    }

    public double getLethalStrainInfectiousness() {
        return lethalStrainInfectiousness;
    }

    public double getLethalStrainMortality() {
        return lethalStrainMortality;
    }

    public double getModerateStrainInfectiousness() {
        return moderateStrainInfectiousness;
    }

    public double getModerateStrainMortality() {
        return moderateStrainMortality;
    }

    public double getMildStrainInfectiousness() {
        return mildStrainInfectiousness;
    }

    public double getMildStrainMortality() {
        return mildStrainMortality;
    }

    public double getLethalStrainBaselinePercentage() {
        return lethalStrainBaselinePercentage;
    }

    public double getModerateStrainBaselinePercentage() {
        return moderateStrainBaselinePercentage;
    }

    public double getMildStrainBaselinePercentage() {
        return mildStrainBaselinePercentage;
    }
}
