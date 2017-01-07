package com.example.tobiasz.projekt;

class Metoda{

    private double stosunek;
    private String nazwa;
    private int[] tab = new int[10];

    Metoda(String wnazwa){
        nazwa = wnazwa;
    }

    Metoda(String wnazwa, int wtab[]){
        nazwa = wnazwa;
        tab = wtab;
    }

    Metoda(String wnazwa, double stos){
        nazwa = wnazwa;
        stosunek = stos;
    }

    String getNazwa(){
        return nazwa;
    }

    int[] getTab() { return tab; }

    double getStosunek(){ return stosunek;}

}
