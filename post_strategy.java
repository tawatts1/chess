import java.util.ArrayList;

public class post_strategy {
    public static ArrayList<int[][]> pawns_first(char[][][] board, ArrayList<int[][]> good_mvs)
    {
        ArrayList<int[][]> out = new ArrayList<int[][]>();
        for (int[][] mv : good_mvs)
        {
            if ('p' == board[mv[0][0]][mv[0][1]][1])
                out.add(mv);
        }
        if (out.size() == 0) // if there are no moves with pawns, return all mvs
            out = good_mvs;
        return out;
    }
    public static ArrayList<int[][]> kill(char[][][] board, ArrayList<int[][]> good_mvs)
    {
        ArrayList<int[][]> out = new ArrayList<int[][]>();
        char color = board[good_mvs.get(0)[0][0]][good_mvs.get(0)[0][1]][0];
        char enemy_color = '0';
        if (color=='b') enemy_color = 'w';
        else if (color == 'w') enemy_color = 'b';

        for (int[][] mv : good_mvs)
        {
            if (enemy_color == board[mv[1][0]][mv[1][1]][0])
                out.add(mv);
        }
        if (out.size() == 0) // if there are no moves with pawns, return all mvs
            out = good_mvs;
        return out;
    }
}
