package zelda.game.entity.collectable;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.game.control.event.EventReward;
import zelda.game.control.text.Message;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;


public abstract class CollectableReward extends EntityObject {
	public static final int TYPE_RAISE    = 0;
	public static final int TYPE_TWO_HAND = 1;
	public static final int TYPE_ONE_HAND = 2;

	protected String name;
	protected int liftType;
	protected Message message;


	// ================== CONSTRUCTORS ================== //

	public CollectableReward(String name, Sprite spr, String messageText, int liftType) {
		super();
		
		hardCollisionBox = new CollisionBox(0, 0, 16, 16);
		softCollisionBox = new CollisionBox(0, 0, 16, 16);
		collideWithWorld = false;
		baseDepth        = 10;
		collideWithFrameBoundaries = true;
		
		this.name     = name;
		this.sprite   = new Sprite(spr);
		this.message  = (messageText.isEmpty() ? null : new Message(messageText));
		this.liftType = liftType;
		
		if (sprite.getSheet() == Resources.SHEET_ICONS_THIN) {
			sprite.getAnimation().getFrame(0).getPart(0).getDrawPos().x = 4;
			hardCollisionBox = new CollisionBox(4, 0, 8, 16);
			softCollisionBox = new CollisionBox(4, 0, 8, 16);
		}
		
		if (name.equals("key"))
			soundBounce = Sounds.OBJECT_KEY_BOUNCE;
	}
	
	public CollectableReward(CollectableReward copy) {
		this(copy.name, new Sprite(copy.sprite), copy.message.getText(), copy.liftType);
	}
	


	// =============== ABSTRACT METHODS =============== //

	public abstract void collect();



	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}

	public int getLiftType() {
		return liftType;
	}

	public Message getMessage() {
		return message;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {
		super.update();
		
		if (Math.abs(zPosition - game.getPlayer().getZPosition()) < 16
			&& Collision.isTouching(softCollidable, game.getPlayer()))
		{
			destroy();
			if (message != null && liftType != TYPE_RAISE) {
				game.playEvent(new EventReward(this));
			}
			else {
				collect();
			}
		}
	}
	
	@Override
	public Vector getShadowDrawPosition() {
		return position.plus(8, 12);
	}
}
