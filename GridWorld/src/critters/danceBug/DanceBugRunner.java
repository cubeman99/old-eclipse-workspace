package critters.danceBug;

import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;


public class DanceBugRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld();
        int[] danceMoves = {4, 1, 0, 2};
        world.add(new Location(0, 3), new DanceBug(danceMoves));
        world.show();
    }
}
