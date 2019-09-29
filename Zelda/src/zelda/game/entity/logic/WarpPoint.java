package zelda.game.entity.logic;

import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.util.Destination;
import zelda.common.util.Direction;
import zelda.game.control.event.Event;
import zelda.game.control.event.EventQueue;
import zelda.game.control.event.EventScreenFade;
import zelda.game.control.event.EventScreenSplit;
import zelda.game.control.transition.TransitionEvent;
import zelda.game.entity.CollisionBox;
import zelda.game.world.Frame;
import zelda.game.world.tile.ObjectTile;


public class WarpPoint extends LogicEntity implements Collidable {
	private static final int TYPE_TUNNEL   = 0;
	private static final int TYPE_STAIRS   = 1;
	private static final int TYPE_ENTRANCE = 2;
	
	private CollisionBox collisionBox;
	private Vector position;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public WarpPoint() {
		super();
		sprite.newAnimation(new Animation(1, 2));
		collisionBox = new CollisionBox(2, 6, 12, 12);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Destination getDestination() {
		return Destination.parseDestination(properties
				.get("destination", ""), game.getWorld());
	}
	
	public int getWarpType() {
		String type = objectData.getProperties().get("type");
		if (type.equals("entrance"))
			return TYPE_ENTRANCE;
		else if (type.equals("stairs"))
			return TYPE_STAIRS;
		else if (type.equals("tunnel"))
			return TYPE_TUNNEL;
		return -1;
	}
	
	public boolean isType(int warpType) {
		return (warpType == getWarpType());
	}
	
	public Event getEnterEvent(Frame frame, final ObjectTile t) {
		EventQueue queue = new EventQueue();
		String warpType = t.getProperties().get("type");
		final Frame checkFrame = frame;
		
		if (warpType.equals("entrance")) {
			queue.addEvent(new Event() {
				private int distance;
				
				@Override
				public void update() {
					game.getPlayer().move(Direction.NORTH);
					if (distance++ >= 24) {
						game.getPlayer().stop();
						end();
					}
				}
			});
		}
		queue.addEvent(new Event() {
			@Override
			public void begin() {
				t.getProperties().script("event_warp_enter", t, checkFrame);
				end();
			}
		});
		return queue;
	}
	
	public Event getExitEvent(TransitionEvent transition) {
		int type = getWarpType();
		
		if (type == TYPE_STAIRS) {
			return new EventQueue(new EventScreenFade(EventScreenFade.FADE_OUT), transition, new EventScreenFade(EventScreenFade.FADE_IN));
		}
		else if (type == TYPE_TUNNEL) {
			return new EventQueue(transition, new EventScreenSplit());
		}
		else if (type == TYPE_ENTRANCE) {
    		return new EventQueue(
				new Event() {
					private int distance;
					
					@Override
					public void update() {
						game.getPlayer().move(Direction.SOUTH);
						if (distance++ >= 24) {
							game.getPlayer().stop();
							end();
						}
					}
				},
				transition, new EventScreenSplit());
		}
		
		return null;
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public boolean warp() {
		Destination dest = getDestination();
		if (dest == null)
			return false;
		
		EventQueue queue = new EventQueue(getExitEvent(new TransitionEvent(dest)));
		ObjectTile t = dest.getWarpTile();
		if (t != null) {
			if (dest.getPosition() != null) {
    			if (t.getEntityData().getProperties().get("type").equals("entrance")) {
    					dest.getPosition().add(0, 8);
    			}
    			else if (t.getEntityData().getProperties().get("type").equals("tunnel")) {
					dest.getPosition().add(0, 1);
    			}
			}
			Event e = getEnterEvent(dest.getFrame(), t);
			if (e != null)
				queue.addEvent(e);
		}
		
		game.playEvent(queue);
		Sounds.WARP_EXIT.play();
		return true;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void begin() {
		super.begin();
		position = new Vector(((ObjectTile)
				objectData.getSource()).getPosition());
		
		if (isType(TYPE_ENTRANCE))
			collisionBox = new CollisionBox(-16, 8, 48, 16);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addEvent("event_warp_enter", "Called after the player warps to this point.");
		objectData.addProperty("destination", "");
		objectData.addProperty("type", "stairs"); // entrance, tunnel, stairs
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		String type = objectData.getProperties().get("type");
		
		if (type.equals("entrance"))
			sprite.newAnimation(new Animation(0, 2));
		else if (type.equals("stairs"))
			sprite.newAnimation(new Animation(1, 2));
		else if (type.equals("tunnel"))
			sprite.newAnimation(new Animation(2, 2));
		
		super.drawTileSprite(pos, frame);
	}
	
	@Override
	public WarpPoint clone() {
		return new WarpPoint();
	}
	
	@Override
	public Vector getPosition() {
		return position;
	}
	
	@Override
	public CollisionBox getCollisionBox() {
		return collisionBox;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
