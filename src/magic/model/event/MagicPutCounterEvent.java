package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicCounterType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;

public class MagicPutCounterEvent extends MagicEvent {

    public MagicPutCounterEvent(final MagicSource source, final MagicCounterType type, final int amount) {
        super(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            amount,
            EventActionTarget(type),
            "PN puts " + amount + " " + type.getName() + " counters on target creature$."
        );
    }

    public MagicPutCounterEvent(final MagicSource source, final int amount) {
        this(source, MagicCounterType.PlusOne, amount);
    }
    
    private static final MagicEventAction EventActionTarget(final MagicCounterType type) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
                event.processTargetPermanent(game,choiceResults,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicChangeCountersAction(
                            creature,
                            type,
                            event.getRefInt(),
                            true
                        ));
                    }
                });
            }
        };
    }
    
    public static final MagicEvent Self(final MagicSource source, final MagicCounterType type, final int amount) {
        return new MagicEvent(
            source,
            amount,
            EventAction(type),
            "PN puts " + amount + " " + type.getName() + " counters on SN."
        );
    }
    
    private static final MagicEventAction EventAction(final MagicCounterType type) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    type,
                    event.getRefInt(),
                    true
                ));
            }
        };
    }
}
