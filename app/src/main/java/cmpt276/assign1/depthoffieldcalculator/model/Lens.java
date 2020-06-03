package cmpt276.assign1.depthoffieldcalculator.model;

/**
 * Stores information about a single lens
 */
public class Lens {
    private String make;
    private double maxAperture;
    private int focalLength;    // [mm]
    private int iconID;

    public Lens(String make, double maxAperture, int focalLength, int iconID){
        this.make = make;
        this.maxAperture = maxAperture;
        this.focalLength = focalLength;
        this.iconID = iconID;
    }

    public String getMake(){
        return make;
    }

    public double getMaxAperture(){
        return maxAperture;
    }

    public int getFocalLength(){
        return focalLength;
    }

    public int getIconID() {
        return iconID;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setMaxAperture(double maxAperture) {
        this.maxAperture = maxAperture;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    @Override
    public String toString() {
        return make
                + " " + focalLength + "mm"
                + " F" + maxAperture;
    }
}
