package cmpt276.assign1.depthoffieldcalculator.model;

/**
 * Compute depth of field values given a lens and some info about the camera settings
 */
public class DepthOfFieldCalculator {
    private Lens lens;
    private double aperture;
    private double distance;
    private double COC;

    public DepthOfFieldCalculator(Lens lens, double aperture, double distance, double COC) {
        this.lens = lens;
        this.aperture = aperture;
        this.distance = distance;
        this.COC = COC;
    }

    public Lens getLens() {
        return lens;
    }

    public double getAperture() {
        return aperture;
    }

    public double getDistance() {
        return distance;
    }

    public double getCOC() {
        return COC;
    }

    public double hyperFocalDistance(){
        return (lens.getFocalLength() * lens.getFocalLength()) / (aperture * COC);
    }

    public double nearFocalPoint(){
        double hyperFocalDistance = hyperFocalDistance();
        return (hyperFocalDistance * distance)
                / (hyperFocalDistance + (distance - COC));
    }

    public double farFocalPoint(){
        double hyperFocalDistance = hyperFocalDistance();
        if(distance > hyperFocalDistance){
            return Double.POSITIVE_INFINITY;
        } else{
            return (hyperFocalDistance * distance)
                    / (hyperFocalDistance - (distance - lens.getFocalLength()));
        }
    }

    public double depthOfField(){
        return farFocalPoint() - nearFocalPoint();
    }

    @Override
    public String toString() {
        return "\tIn focus: " + nearFocalPoint() + "m to " + farFocalPoint()
                + "m [DoF = " + depthOfField() + "m]"
                + "\n\tHyperfocal point: " + hyperFocalDistance() + "m";
    }
}
