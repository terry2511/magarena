[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "+1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Charge,
                    1
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PERMANENT,
                new MagicNoCombatTargetPicker(true,true,false),
                this,
                "Target permanent\$ doesn't untap during its controller's next untap step."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeStateAction(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext,
                        true
                    ));
                }
            });
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(2)
        ],
        new MagicActivationHints(MagicTiming.Main),
        "-2") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    2
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature or player\$ and PN gains 3 life."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,3,false);
                    game.doAction(new MagicDealDamageAction(damage));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
                }
            });
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(7)
        ],
        new MagicActivationHints(MagicTiming.Main),
        "-7") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    7
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Destroy all lands target player\$ controls."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(player,MagicTargetFilter.TARGET_LAND_YOU_CONTROL);            
                    game.doAction(new MagicDestroyAction(targets));
                }
            });
        }
    }
]
