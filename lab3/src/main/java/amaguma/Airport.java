package amaguma;

import scala.Serializable;

public class Airport implements Serializable {
    public String name;

    Airport() {}

    Airport(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
