package me.hellonayeon.crdt.sequence;

public class Element {
    private String value;
    private Long id;

    public Element(String value, Long id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }
}
