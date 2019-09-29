package zelda.game.monster;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.reactions.ReactionCause;
import zelda.common.reactions.ReactionEffect;
import zelda.common.util.GMath;
import zelda.game.control.text.Message;
import zelda.game.entity.Entity;
import zelda.main.Keyboard;


public class MonsterCukeman extends BasicMonster {
	private String[] phrases;

	public MonsterCukeman() {
		super();

		health.fill(1);

		moveSpeed = 0.25;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 50;
		moveTimeMax = 80;
		numDirectionAngles = 8;

		phrases = new String[] {
			"Feel my cold, steely gaze!!!",
			"Hu? Did I say that?",
			"I wish I could go to a tropical southern island.",
			"Really? I mean, I knew that!",
			"I want to ride a plane. Anywhere<ap>s fine.",
			"3 Large,<n>2 Regular.", "I<ap>m so sleepy.",
			"I want a nice tropical vacation."
		};

		sprite.newAnimation(new Animation().addFrame(8, 11, 16)
				.addFrame(8, 12, 16).addFrame(8, 13, 16).addFrame(8, 12, 16));

		setReaction(ReactionCause.SWORD, new ELECTROCUTE());
		setReaction(ReactionCause.BIGGORON_SWORD, new ELECTROCUTE());
		setReaction(ReactionCause.SWITCH_HOOK, new ELECTROCUTE());
		setReaction(ReactionCause.SCENT_SEED, new KILL());
		setReaction(ReactionCause.MYSTERY_SEED, null);
		setReaction(ReactionCause.TALK, new CATCH_PHRASES());
	}

	@Override
	public MonsterCukeman clone() {
		return new MonsterCukeman();
	}

	protected final class CATCH_PHRASES implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			int index = GMath.random.nextInt(phrases.length);
			game.readMessage(new Message(phrases[index]));
			Keyboard.a.clear();
		}
	}
}
