import java.util.ArrayList;
import java.util.Arrays;


public class operations {
    public  static final char[] EMPTY = { '0', '0' };
    public  static final char[] BQ = { 'b', 'q' };
    public  static final char[] WQ = { 'w', 'q' };
    public static final char[] BP = { 'b', 'p' };
    public static final char[] WP = { 'w', 'p' };
    public static final char[] BK = { 'b', 'k' };
    public static final char[] WK = { 'w', 'k' };
    public static final char[] BB = { 'b', 'b' };
    public static final char[] WB = { 'w', 'b' };
    public static final char[] BN = { 'b', 'n' };
    public static final char[] WN = { 'w', 'n' };
    public static final char[] BR = { 'b', 'r' };
    public static final char[] WR = { 'w', 'r' };
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
        board1[c1[0]][c1[1]] = EMPTY; 
        //handle queen promotion:
        if (piece[1] == 'p' && piece[0]=='b' && board1[c2[0]][c2[1]][0] == 7)
            board1[c2[0]][c2[1]] = BQ;
        else if (piece[1] == 'p' && piece[0]=='w' && board1[c2[0]][c2[1]][0] == 0)
            board1[c2[0]][c2[1]] = WQ;
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

  public static int max_index(byte[] arr)
  {
    //byte out = 0;
    int max_i = 0;
    for (int i=0; i<arr.length; i++)
    {
      if (arr[i] > arr[max_i])
      {
        max_i = i;
      }
    }
    //System.out.println(arr[max_i]);
    return max_i;
  }

  public static char[][][] construct_board(String lst)
  {
    char[][][] out = new char[8][8][2];
    String[] rows = lst.split("=");
    char color;
    char name;
    for (byte i=0; i<8; i++)
    {
      //System.out.println(row);
      char[][] row_l = new char[8][2];
      String[] pieces = rows[i].split("_");
      for (byte j=0; j<8; j++)
      {
        color = pieces[j].charAt(0);
        if (color=='0')
        {
          row_l[j] = EMPTY;
        }
        else if (color=='b')
        {
          name = pieces[j].charAt(1);
          switch (name)
          {
            case 'p': row_l[j] = BP; break;
            case 'q': row_l[j] = BQ; break;
            case 'k': row_l[j] = BK; break;
            case 'b': row_l[j] = BB; break;
            case 'n': row_l[j] = BN; break;
            case 'r': row_l[j] = BR; break; 
          }
        }
        else if (color == 'w')
        {
          name = pieces[j].charAt(1);
          switch (name)
          {
            case 'p': row_l[j] = WP; break;
            case 'q': row_l[j] = WQ; break;
            case 'k': row_l[j] = WK; break;
            case 'b': row_l[j] = WB; break;
            case 'n': row_l[j] = WN; break;
            case 'r': row_l[j] = WR; break; 
          }
        }
        
      }
      out[i] = row_l;
    }
    return out;
  }
  public static byte[] add_lists(byte[] l1, byte[] l2)
  {
    int size = l1.length;
    byte[] out = new byte[size];
    for (int i=0; i<size; i++)
    {
      out[i] = (byte) (l1[i] + l2[i]);
    }
    return out;
  }
  public static byte[] multiply_list(byte[] l1, byte alpha)
  {
    int size = l1.length;
    byte[] out = new byte[size];
    for (int i=0; i<size; i++)
    {
      out[i] = (byte) (l1[i] * alpha);
    }
    return out;
  }
  public static void print_board(char[][][] board)
  {
    for (char[][] row : board)
    {
      String row_str = "";
      for (char[] piece : row)
      {
        row_str += "" + piece[0] + piece[1] + "  ";
      }
      System.out.println(row_str);
    }
  }

}
