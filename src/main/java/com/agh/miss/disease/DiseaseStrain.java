package com.agh.miss.disease;

public abstract class DiseaseStrain {
    protected double infectiousness;
    protected double mortality;
    protected double curability;

    public double getInfectiounsness() {
        return infectiousness;
    }

    public double getMortality() {
        return mortality;
    }

    public double getCurability() {
        return curability;
    }
}
