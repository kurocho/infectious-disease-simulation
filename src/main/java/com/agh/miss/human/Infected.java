package com.agh.miss.human;

import com.agh.miss.disease.DiseaseStrain;
import com.agh.miss.disease.StrainType;

public class Infected extends Human {

    private StrainType strainType;
    private DiseaseStrain diseaseStrain;

    public Infected(Human human, StrainType strainType, DiseaseStrain diseaseStrain) {
        super(human);
        this.strainType = strainType;
        this.diseaseStrain = diseaseStrain;
        setStyle();
    }


    @Override
    protected void setStyle() {

        switch (strainType) {
            case LETHAL:
                node.setAttribute("ui.class", "infected_lethal");
                break;
            case MODERATE:
                node.setAttribute("ui.class", "infected_moderate");
                break;
            case MILD:
                node.setAttribute("ui.class", "infected_mild");
                break;
        }
    }

    @Override
    public boolean isInfected() {
        return true;
    }

    public DiseaseStrain getDiseaseStrain() {
        return diseaseStrain;
    }
}
