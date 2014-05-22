[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.PlusOne,
                    2,
                    true
                ));
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.FirstStrike,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    }
]
