package cmpt276.assign1.depthoffieldcalculator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores a collection of lenses
 */
public class LensManager implements Iterable<Lens>{
    private List<Lens> lenses = new ArrayList<>();

    private static LensManager instance;
    private LensManager(){
        // Empty
        // Prevents anyone else from instantiating
    }
    public static LensManager getInstance(){
        if(instance == null){
            instance = new LensManager();
        }
        return instance;
    }

    public List<Lens> getLenses() {
        return lenses;
    }

    public int getSize() {
        return lenses.size();
    }

    public Lens get(int indx){
        return lenses.get(indx);
    }

    public void setLenses(List<Lens> lenses) {
        this.lenses = lenses;
    }

    public void add(Lens lens){
        lenses.add(lens);
    }

    public void remove(Lens lens){
        lenses.remove(lens);
    }

    @Override
    public Iterator<Lens> iterator() {
        return lenses.iterator();
    }
}
