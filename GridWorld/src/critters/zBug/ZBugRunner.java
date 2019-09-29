package critters.zBug;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;


public class ZBugRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld();
        world.add(new Location(1, 1), new ZBug(7));
        world.show();
    }
}
