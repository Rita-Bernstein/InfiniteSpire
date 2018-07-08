package infinitespire.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;

public class BlanksBlanky extends Relic {

	public static final String ID = "Blanks Blanky";
	
	public BlanksBlanky() {
		super(ID, "blanksblanky", RelicTier.SPECIAL, LandingSound.MAGICAL);
	}

	@Override
	public AbstractRelic makeCopy() {
		return new BlanksBlanky();
	}

	@Override
	public void onRest() {
		InfiniteSpire.logger.info("InfiniteSpire | Healing player with Blank's Blanky.");
		AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
	}
}
