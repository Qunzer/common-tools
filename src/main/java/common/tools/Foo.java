package common.tools;

import java.io.Serializable;

/**
 * Created by yuanrq on 2017/04/14.
 */
class Foo implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
