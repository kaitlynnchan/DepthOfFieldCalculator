package cmpt276.assign1.depthoffieldcalculator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores a collection of lenses
 */
public class LensManager implements Iterable<Lens>{
    private List<Lens> lenses = new ArrayList<>();

    public void add(Lens lens){
        lenses.add(lens);
    }

    public int getSize() {
        return lenses.size();
    }

    public Lens getLens(int indx){
        return lenses.get(indx);
    }

    @Override
    public Iterator<Lens> iterator() {
        return lenses.iterator();
    }
}
