import java.util.ArrayList;

public class GameAgent {

    GameAgent() { }

    public Move minimaxMove(MainBoard mb, int depth, int player) {
        ArrayList<Move> moves = mb.getChildren(player);

        int bestscore = 0;
        int bestindex = 0;
        int score;

        for (int i = 0; i < moves.size(); i++) {
            score = minimax(moves.get(i).getBoard(), depth, player);
            if (player == 1) {
                if (score > bestscore) {
                    bestscore = score;
                    bestindex = i;
                }
            } else {
                if (score < bestscore) {
                    bestscore = score;
                    bestindex = i;
                }
            }
            moves.get(i).setValue(score);
        }
        return moves.get(bestindex);
    }

    private int minimax(MainBoard node, int depth, int player) {
        if (depth == 0 || node.checkVictory(player) || node.checkFull()) {
            return node.heuristic();
        }
        if (player == 1) {
            int value = Integer.MIN_VALUE;
            for (Move child : node.getChildren(player)) {
                value = Math.max(value, minimax(child.getBoard(), depth-1, 2));
            }
            return value;
        }
        else {
            int value = Integer.MAX_VALUE;
            for (Move child : node.getChildren(player)) {
                value = Math.min(value, minimax(child.getBoard(), depth-1, 1));
            }
            return value;
        }
    }
}
