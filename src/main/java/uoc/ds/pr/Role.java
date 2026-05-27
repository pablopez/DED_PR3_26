package uoc.ds.pr;

public enum Role {
    DIRECTOR("Director", 5),
    ASSISTANT_DIRECTOR("Assistant Director", 4),
    UNIT_HEAD("Unit Head", 3),
    MANAGER("Manager", 2),
    ANALYST("Analyst", 1);

    private final String label;
    private final int rank;

    Role(String label, int rank) {
        this.label = label;
        this.rank = rank;
    }

    public String getLabel() {
        return label;
    }

    public int getRank() {
        return rank;
    }

    public static Role fromString(String text) {
        for (Role r : Role.values()) {
            if (r.label.equalsIgnoreCase(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Role not found for: " + text);
    }

}