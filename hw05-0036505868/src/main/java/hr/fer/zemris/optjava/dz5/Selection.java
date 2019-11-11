package hr.fer.zemris.optjava.dz5;

import java.util.Collection;
import java.util.List;

public interface Selection<T> {

    public T select(List<T> population);

}
