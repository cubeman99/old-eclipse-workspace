package saveMyHeart;

import java.awt.Color;
import saveMyHeart.bacterium.Bacterium;
import saveMyHeart.heart.Heart;
import saveMyHeart.whiteBloodCell.WhiteBloodCell;

import common.GMath;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.BoundedGrid;
import info.gridworld.grid.Location;
import info.gridworld.grid.UnboundedGrid;


public class SaveMyHeartRunner {
    public static void main(String[] args) {
        ActorWorld world = new ActorWorld();//new BoundedGrid<Actor>(40, 40));
        
        // Add bacteria to the world:
        for (int i = 0; i < 1; i++)
        	world.add(new Bacterium(Color.BLUE));
        
        // Add white blood cells to the world:
        for (int i = 0; i < 1; i++)
        	world.add(new WhiteBloodCell());
        
        // Add hearts to the world:
        for (int i = 0; i < 1; i++)
        	world.add(new Heart());
        
        world.show();
    }
}
