package cmpt276.assign1.depthoffieldcalculator.model;

/**
 * Stores information about a single lens
 * info: make, maximum aperture, focal length
 */
public class Lens {
    private String make;
    private double maxAperture;
    private int focalLength;

    public Lens(String make, double maxAperture, int focalLength){
        this.make = make;
        this.maxAperture = maxAperture;
        this.focalLength = focalLength;
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

    @Override
    public String toString() {
        return make
                + " F" + maxAperture
                + " " + focalLength + "mm";
    }
}
