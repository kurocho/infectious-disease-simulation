package com.agh.miss.disease;

import com.agh.miss.ConfigurationProvider;

public class MildStrain extends DiseaseStrain {
    private static MildStrain mildStrain = new MildStrain();

    public static MildStrain getInstance() {
        return mildStrain;
    }

    private MildStrain() {
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        this.infectiousness = configurationProvider.getMildStrainInfectiousness();
        this.mortality = configurationProvider.getMildStrainMortality();
    }
}
