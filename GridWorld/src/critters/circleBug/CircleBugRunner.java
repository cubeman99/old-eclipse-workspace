package critters.circleBug;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;
import info.gridworld.grid.UnboundedGrid;


public class CircleBugRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld(new UnboundedGrid<Actor>());
        world.add(new Location(14, 4), new CircleBug(10));
        world.show();
    }
}
