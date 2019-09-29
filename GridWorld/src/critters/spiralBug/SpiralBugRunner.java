package critters.spiralBug;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;
import info.gridworld.grid.UnboundedGrid;


public class SpiralBugRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld(new UnboundedGrid<Actor>());
        world.add(new Location(16, 16), new SpiralBug());
        world.show();
    }
}
