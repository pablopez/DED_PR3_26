package uoc.ds.pr.model;

public abstract class AbstractModel implements Identifiable, Comparable<AbstractModel> {
    protected String id;

    public AbstractModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(AbstractModel other) {
        if (other == null) {
            return 1;
        }
        return this.id.compareTo(other.getId());
    }
}