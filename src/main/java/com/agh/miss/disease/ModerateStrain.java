package com.agh.miss.disease;

import com.agh.miss.ConfigurationProvider;

public class ModerateStrain extends DiseaseStrain {
    private static ModerateStrain moderateStrain = new ModerateStrain();

    public static ModerateStrain getInstance(){
        return moderateStrain;
    }

    private ModerateStrain(){
        ConfigurationProvider configurationProvider = ConfigurationProvider.getInstance();
        this.infectiousness = configurationProvider.getModerateStrainInfectiousness();
        this.mortality = configurationProvider.getModerateStrainMortality();
        this.curability = configurationProvider.getModerateStrainCurability();
    }
}
