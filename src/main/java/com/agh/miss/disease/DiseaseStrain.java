package com.agh.miss.disease;

public abstract class DiseaseStrain {
    protected double infectiousness;
    protected double mortality;

    public double getInfectiounsness() {
        return infectiousness;
    }

    public double getMortality() {
        return mortality;
    }
}
