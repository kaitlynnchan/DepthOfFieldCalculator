package cmpt276.assign1.depthoffieldcalculator.model;

/**
 * Compute depth of field values given a lens and some info about the camera settings
 */
public class DepthOfFieldCalculator {
    private Lens lens;
    private double aperture;
    private double distance;    // [mm]
    private double COC;         // [mm]

    public DepthOfFieldCalculator(Lens lens, double aperture, double distance, double COC) {
        this.lens = lens;
        this.aperture = aperture;
        this.distance = distance;
        this.COC = COC;
    }

    public double hyperfocalDistance(){
        return (lens.getFocalLength() * lens.getFocalLength()) / (aperture * COC);
    }

    public double nearFocalPoint(){
        double hyperfocalDistance = hyperfocalDistance();
        return (hyperfocalDistance * distance)
                / (hyperfocalDistance + (distance - COC));
    }

    public double farFocalPoint(){
        double hyperfocalDistance = hyperfocalDistance();
        if(distance > hyperfocalDistance){
            return Double.POSITIVE_INFINITY;
        } else{
            return (hyperfocalDistance * distance)
                    / (hyperfocalDistance - (distance - lens.getFocalLength()));
        }
    }

    public double depthOfField(){
        return farFocalPoint() - nearFocalPoint();
    }

}
