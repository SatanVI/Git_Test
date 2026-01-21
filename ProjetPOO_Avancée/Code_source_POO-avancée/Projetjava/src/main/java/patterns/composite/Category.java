package patterns.composite;

import java.util.ArrayList;
import java.util.List;

public class Category implements Component {
    private final String name;
    private final List<Component> children = new ArrayList<>();

    public Category(String name) { this.name = name; }

    public void add(Component c) { children.add(c); }
    public void remove(Component c) { children.remove(c); }

    @Override
    public String getName() { return name; }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Category: " + name);
        for (Component c : children) {
            c.print(indent + "  ");
        }
    }
}
