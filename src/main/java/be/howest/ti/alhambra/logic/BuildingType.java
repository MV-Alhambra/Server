package be.howest.ti.alhambra.logic;

public enum BuildingType {
    FOUNTAIN, PAVILION, SERAGLIO, ARCADES, CHAMBERS, GARDEN, TOWER;

    @Override
    public String toString(){
        return name().toLowerCase();
    }
}
