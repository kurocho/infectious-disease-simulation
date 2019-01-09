package com.agh.miss.disease;

import com.agh.miss.ConfigurationProvider;

public class DiseaseStrainProvider {
    private double lethalStrainBaselinePercentage;
    private double moderateStrainBaselinePercentage;
    private double mildStrainBaselinePercentage;

    private static DiseaseStrainProvider diseaseStrainProvider = new DiseaseStrainProvider();

    public static DiseaseStrainProvider getInstance() {
        return diseaseStrainProvider;
    }

    DiseaseStrainProvider() {
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        lethalStrainBaselinePercentage = configurationProvider.getLethalStrainBaselinePercentage();
        moderateStrainBaselinePercentage = configurationProvider.getModerateStrainBaselinePercentage() + lethalStrainBaselinePercentage;
        mildStrainBaselinePercentage = configurationProvider.getMildStrainBaselinePercentage() + moderateStrainBaselinePercentage;
    }

    public StrainType getRandomStrain() {
        double randomFraction = 100 * Math.random();
        if (randomFraction < lethalStrainBaselinePercentage) {
            return StrainType.LETHAL;
        } else if (randomFraction < moderateStrainBaselinePercentage) {
            return StrainType.MODERATE;
        } else {
            return StrainType.MILD;
        }
    }

    public DiseaseStrain getStrain(StrainType strainType) {
        DiseaseStrain diseaseStrain = null;
        switch (strainType) {
            case LETHAL:
                diseaseStrain = LethalStrain.getInstance();
                break;
            case MODERATE:
                diseaseStrain = ModerateStrain.getInstance();
                break;
            case MILD:
                diseaseStrain = MildStrain.getInstance();
                break;
        }
        return diseaseStrain;
    }
}
