package amaguma;

import scala.Serializable;

public class Airport implements Serializable {
    public String name;
    public int id;

    Airport() {}

    Airport(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
