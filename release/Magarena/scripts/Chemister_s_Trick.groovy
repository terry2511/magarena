[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_DONT_CONTROL,
                new MagicTapTargetPicker(true,false),
                this,
                "Target creature\$ you don't control gets -2/-0 until end of turn and attacks this turn if able."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature, -2, 0));
                    game.doAction(new MagicSetAbilityAction(creature, MagicAbility.AttacksEachTurnIfAble));
                }
            });
        }
    },
    new MagicCardActivation(
        [
            MagicConditionFactory.ManaCost("{3}{U}{R}")
        ],
        new MagicActivationHints(MagicTiming.Tapping,true),
        "Overload"
    ) {
        public MagicEvent[] getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, source.getController(), MagicManaCost.create("{3}{U}{R}"))
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature you don't control gets -2/-0 until end of turn and attacks this turn if able."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature, -2, 0));
                game.doAction(new MagicSetAbilityAction(creature, MagicAbility.AttacksEachTurnIfAble));
            }
        }
    }
]
