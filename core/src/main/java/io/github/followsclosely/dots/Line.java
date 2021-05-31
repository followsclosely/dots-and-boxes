package io.github.followsclosely.dots;


import java.util.List;

public interface Line {
    int getPlayer();
    List<?extends Box> getParents();
}
