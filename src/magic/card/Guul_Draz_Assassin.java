package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicWeakenTargetPicker;

public class Guul_Draz_Assassin {
    public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 4) {
                pt.set(4,4);
            } else if (charges >= 2) {
                pt.set(2,2);
            }
        }        
    };

    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicConditionFactory.ChargeCountersAtLeast(2),
                MagicCondition.CAN_TAP_CONDITION,
                MagicConditionFactory.ManaCost("{B}")
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Weaken") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                    new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{B}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            final int amount = source.getCounters(MagicCounterType.Charge) >= 4 ? -4:-2;
            return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(amount,amount),
                    amount,
                    this,
                    "Target creature$ gets " + amount + "/" + amount + " until end of turn.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amount = event.getRefInt();
            event.processTargetPermanent(game,choiceResults,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
                }
            });
        }
    };
}
