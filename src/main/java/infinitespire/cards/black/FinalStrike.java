package infinitespire.cards.black;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.effects.uniqueVFX.FinalStrikeEffect;

public class FinalStrike extends BlackCard {

	public static final String ID = InfiniteSpire.createID("FinalStrike");

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String IMG = "img/infinitespire/cards/finalstrike.png";
	private static final int COST = 2;

	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardTarget TARGET = CardTarget.ENEMY;
	
	public FinalStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		this.baseDamage = 10;
		this.baseMagicNumber = 5;
		this.magicNumber = 5;
		this.tags.add(CardTags.STRIKE);
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new FinalStrike();
	}
	
	public static boolean isStrike(AbstractCard card) {
		boolean retVal = PerfectedStrike.isStrike(card);
		
		if(card.cardID.toLowerCase().contains("strike")) {
			return true;
		}
		
		return retVal;
	}
	
	

	@Override
	public void applyPowers() {
		float tmp = this.baseDamage;
		tmp += this.magicNumber * countCards();
		for(AbstractPower p : AbstractDungeon.player.powers) {
			tmp = p.atDamageGive(tmp, this.damageTypeForTurn);
		}
		for(AbstractPower p : AbstractDungeon.player.powers) {
			tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn);
		}
		
		if(tmp < 0.0f) {
			tmp = 0.0f;
		}
		
		if(this.baseDamage != (int)tmp) {
			this.isDamageModified = true;
		}
		
		this.damage = MathUtils.floor(tmp);
	}
	
	

	@Override
	public void calculateCardDamage(AbstractMonster m) {
		float tmp = this.baseDamage;
		tmp += this.magicNumber * countCards();
		for(AbstractPower p : AbstractDungeon.player.powers) {
			tmp = p.atDamageGive(tmp, this.damageTypeForTurn);
		}
		for(AbstractPower p : AbstractDungeon.player.powers) {
			tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn);
		}
		if(m != null) {
			for(AbstractPower p : m.powers) {
				tmp = p.atDamageReceive(tmp, this.damageTypeForTurn);
			}
			for(AbstractPower p : m.powers) {
				tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn);
			}
		}
		if(tmp < 0.0f) {
			tmp = 0.0f;
		}
		
		if(this.baseDamage != (int)tmp) {
			this.isDamageModified = true;
		}
		
		this.damage = MathUtils.floor(tmp);
	}

	private static int countCards() {
		int count = 0;
		for(AbstractCard card : AbstractDungeon.player.hand.group)
			if(isStrike(card) || card.tags.contains(CardTags.STRIKE)) count++;
		
		for(AbstractCard card : AbstractDungeon.player.drawPile.group) 
			if(isStrike(card) || card.tags.contains(CardTags.STRIKE)) count++;
		
		for(AbstractCard card : AbstractDungeon.player.discardPile.group)
			if(isStrike(card) || card.tags.contains(CardTags.STRIKE)) count++;
		
		return count;
	}

	@Override
	public void upgrade() {
		if(!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(5);
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if(m != null) AbstractDungeon.actionManager.addToBottom(new VFXAction(new FinalStrikeEffect(m.hb.cX, m.hb.cY)));

		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8f));
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffect.NONE));
	}

}
