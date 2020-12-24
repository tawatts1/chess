import java.util.ArrayList;
import java.util.Collections;

public class ai_util 
{

    public static void prioritize_moves(char[][][] board, ArrayList<int[][]> mvs0, char enemy_color)
    {  
      int[][] mv = new int[2][2];
      int j = 0;
      for (int i=0; i< mvs0.size(); i++)
      {
        mv = mvs0.get(i);
        if (board[mv[1][0]][mv[1][1]][0] == enemy_color)
        {
          Collections.swap(mvs0, i, j++); // swap elements and increment j. 
        }
      }
    }

    public static int piece_value(char piece)
    {
        int out=0;
        switch (piece)
        {
            case 'p' : out=100; break;
            case 'b' : 
            case 'n' : out = 300; break;
            case 'r' : out = 500; break;
            case 'q' : out = 900; break;
            default  : break;
        }
        return out;
    }
    
}
