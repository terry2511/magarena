package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicPlayTokensAction extends MagicAction {

    private final MagicCard card;
    private final int count;

    public MagicPlayTokensAction(final MagicPlayer player, final MagicCardDefinition cardDefinition, final int aCount) {
        card  = MagicCard.createTokenCard(cardDefinition,player);
        count = aCount;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        for (int i = 0; i < count; i++) {
            game.doAction(new MagicPlayTokenAction(card));
        }
    }
    
    @Override
    public void undoAction(final MagicGame game) {
        //empty
    }
}
