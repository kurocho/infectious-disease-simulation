package com.agh.miss.disease;

import com.agh.miss.ConfigurationProvider;

public class LethalStrain extends DiseaseStrain {
    private static LethalStrain lethalStrain = new LethalStrain();

    public static LethalStrain getInstance(){
        return lethalStrain;
    }

    private LethalStrain(){
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        this.infectiousness = configurationProvider.getLethalStrainInfectiousness();
        this.mortality = configurationProvider.getLethalStrainMortality();
    }
}
