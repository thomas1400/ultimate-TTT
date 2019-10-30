import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameAgent {

    GameAgent() { }

    public Move minimaxMove(MainBoard mb, int depth, int player) {
        ArrayList<Move> moves = mb.getChildren(player);

        int bestscore;
        if (player == 1) {
            bestscore = Integer.MIN_VALUE;
        } else {
            bestscore = Integer.MAX_VALUE;
        }
        int score;

        for (Move move : moves) {
            score = minimax(move.getBoard(), depth, 3 - player);
            if (player == 1) {
                if (score > bestscore) {
                    bestscore = score;
                }
            } else {
                if (score < bestscore) {
                    bestscore = score;
                }
            }
            move.setValue(score);
        }

        ArrayList<Move> options = new ArrayList<Move>();
        for (Move m : moves) {
            if (m.getValue() == bestscore) {
                options.add(m);
            }
        }

        return options.get(new Random().nextInt(options.size()));
    }

    public Move alphabetaMove(MainBoard mb, int depth, int player) {
        ArrayList<Move> moves = mb.getChildren(player);

        int bestscore;
        if (player == 1) {
            bestscore = Integer.MIN_VALUE;
        } else {
            bestscore = Integer.MAX_VALUE;
        }
        int score;

        for (Move move : moves) {
            score = alphabeta(move.getBoard(), depth, 3 - player);
            if (player == 1) {
                if (score > bestscore) {
                    bestscore = score;
                }
            } else {
                if (score < bestscore) {
                    bestscore = score;
                }
            }
            move.setValue(score);
        }

        ArrayList<Move> options = new ArrayList<Move>();
        for (Move m : moves) {
            if (m.getValue() == bestscore) {
                options.add(m);
            }
        }

        return options.get(new Random().nextInt(options.size()));
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

    private int alphabeta(MainBoard node, int depth, int a, int b, int player) {
        if (depth == 0 || node.checkVictory(player) || node.checkFull()) {
            return node.heuristic();
        }
        if (player == 1) {
            int value = Integer.MIN_VALUE;
            for (Move child : node.getChildren(player)) {
                value = Math.max(value, alphabeta(child.getBoard(),depth-1, a, b, 2));
                a = Math.max(a, value);
                if (a >= b) {
                    break;
                }
            }
            return value;
        }
        else {
            int value = Integer.MAX_VALUE;
            for (Move child : node.getChildren(player)) {
                value = Math.min(value, alphabeta(child.getBoard(), depth-1, a, b,1));
                b = Math.min(b, value);
                if (a >= b) {
                    break;
                }
            }
            return value;
        }
    }

    private int alphabeta(MainBoard board, int depth, int player) {
        return alphabeta(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
    }
}
