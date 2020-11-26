import java.util.ArrayList;
import java.util.Arrays;


public class operations {
    public static char[][][] execute_move(char[][][] board, byte[] c1, byte[] c2)
    {
        char[][][] board1 = new char[8][8][2];
        for (byte i=0; i<8; i++)
        {
            if (c1[0] == i || c2[0] == i)
            {
            
            for (byte j=0; j<8; j++)
            {
                if ((c1[0] == i && c1[1] == j) || (c2[0] == i && c2[1] == j))// if it's a hit
                {
                board1[i][j] = board[i][j].clone();
                }
                else
                {
                board1[i][j] = board[i][j]; // no clone
                }
            }
            
            //board1[i] = board[i].clone();
            }
            else
            {
            board1[i] = board[i]; // no clone
            }
            
        }
        char[] piece = board1[c1[0]][c1[1]];
        board1[c2[0]][c2[1]] = piece;
        board1[c1[0]][c1[1]] = next_move.EMPTY; 
        //handle queen promotion:
        if (piece[1] == 'p' && piece[0]=='b' && board1[c2[0]][c2[1]][0] == 7)
            board1[c2[0]][c2[1]] = next_move.BQ;
        else if (piece[1] == 'p' && piece[0]=='w' && board1[c2[0]][c2[1]][0] == 0)
            board1[c2[0]][c2[1]] = next_move.WQ;
        return board1;
    }
    public static ArrayList<ArrayList<byte[]>> update_coords(ArrayList<byte[]> my_coords, 
                                                    ArrayList<byte[]> enemy_coords, 
                                                    byte[][] move)
  {
    //make copy:
    ArrayList<ArrayList<byte[]>> out = new ArrayList<ArrayList<byte[]>>();//ArrayList[2];//{new ArrayList<byte[]>(my_coords)};
    ArrayList<byte[]> out0 = new ArrayList<byte[]>(my_coords);
    ArrayList<byte[]> out1 = new ArrayList<byte[]>(enemy_coords);
    byte[] temp = {0,0};
    for (int i=0; i<my_coords.size(); i++)
    {
      temp = my_coords.get(i);
      if (temp[0]==move[0][0] && temp[1] == move[0][1])
      {
        out0.set(i,move[1]);
        remove_if_contains(out1, move[1]);
      }
    }
    out.add(out0); out.add(out1);
    return out;
  } 
  private static void remove_if_contains(ArrayList<byte[]> arr, byte[] id)
  {
    for (int i=0; i<arr.size(); i++)
    {
      if (Arrays.equals(arr.get(i), id))
      {
        arr.remove(i);
        break;
      }
    }
  } 
}
