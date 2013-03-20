package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;

public class Vines_of_Vastwood {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(
                        MagicTargetChoice.TARGET_CREATURE,
                        MagicManaCost.create("{G}"),
                        false),
                    MagicPumpTargetPicker.create(),
                    this,
                    "Target creature$ can't be the target of spells or abilities your opponent controls this turn. " + 
                    "If SN was kicked$, that creature gets +4/+4 until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer player = event.getPlayer();
                    if (player.getIndex() == 0) {
                        game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBeTheTarget1));
                    } else {
                        game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBeTheTarget0));
                    }
                    if (((Integer)choiceResults[1])>0) {
                        game.doAction(new MagicChangeTurnPTAction(creature,4,4));
                    }
                }
            });
        }
    };
}
