package com.agh.miss;

public class Stats {
    private int birthCounter = 0;
    private int naturalDeathCounter = 0;
    private int diseaseDeathCounter = 0;
    private int infectedCounter = 0;
    private int curedCounter = 0;

    public void incBirth() {
        this.birthCounter++;
    }

    public void incNaturalDeaths() {
        this.naturalDeathCounter++;
    }

    public void incDiseaseDeaths() {
        this.diseaseDeathCounter++;
    }

    public void incInfected() {
        this.infectedCounter++;
    }

    public void incCured() {
        this.curedCounter++;
    }

    public void summary(){
        System.out.println("###########################");
        System.out.println(naturalDeathCounter+diseaseDeathCounter + " died in this simulation, "+ naturalDeathCounter+ " dead by natural causes and "+ diseaseDeathCounter+ " dead by disease");
        System.out.println(birthCounter + " born");
        System.out.println(infectedCounter + " infected");
        System.out.println(curedCounter + " immune");
        System.out.println("###########################");
        System.out.println();
    }

    public void setBorn(int born) {
        this.birthCounter = born;
    }

    public int getInfectedCounter() {
        return infectedCounter;
    }

    public int getCuredCounter() {
        return curedCounter;
    }
}
