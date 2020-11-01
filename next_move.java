import java.util.ArrayList;
import java.util.List;

/** 
 * "br_00_bb_bq_bk_bb_bn_br=bp_bp_bp_bp_bp_bp_bp_bp=00_00_bn_00_00_00_00_00=00_00_00_00_00_00_00_00=00_00_00_wp_00_00_00_00=00_00_00_00_wp_00_00_00=wp_wp_wp_00_00_wp_wp_wp=wr_wn_wb_wq_wk_wb_wn_wr"
 * "br_00_bb_bq_bk_bb_bn_br=00_00_00_00_00_00_00_00=00_00_bn_00_00_00_00_00=00_00_00_00_00_00_00_00=00_00_00_wp_00_00_00_00=00_00_00_00_wp_00_00_00=wp_wp_wp_00_00_wp_wp_wp=wr_wn_wb_wq_wk_wb_wn_wr"

 * 
 * 
 * **/

public class next_move
{
  public static void main (String[] args)
  {
    //int M = args.size();
    //ArrayList<ArrayList<String>> board0 = construct_board(args[0]);
    char[][][] board = construct_board(args[0]);
    byte[] mv0 = {0,2};
    
    ArrayList<byte[]> mvs = moves_pre_check(board, mv0, 'b');
    
    print_board(board);
    for (byte[] mv : mvs)
    {
      System.out.println(mv[0] + ", " + mv[1]);
    }
    //System.out.println(args[0]);
    //str_to_array("hshs ldld ldld ll");
  }
  
  public static ArrayList<byte[]> moves_pre_check(char[][][] board, byte[] coords, char color)
  {
    ArrayList<byte[]> out = new ArrayList<byte[]>();
    //byte[][] out = {{0,0},{0,0}};
    char piece = board[coords[0]][coords[1]][1];
    char piece_color = board[coords[0]][coords[1]][0];
    if (piece_color==color)
    {
      switch (piece)
      {
        case 'n': 
          byte[][] moves = {{2,1}, {2,-1}, 
                            {1,2}, {1,-2}, 
                            {-1,2},{-1,-2}, 
                            {-2,1},{-2,-1} };
          for (byte[] move : moves)//(byte i=0; i<8; i++)
          {
            byte[] out0 = add_lists(coords, move);
            if (in_board_space(out0))
            {
              //byte[][] mv = {coords, out0};
              out.add(out0);
            }
          }
          break;
        case 'b':
          byte[][] basis = {{1,-1},{1,1}};
          byte[] sign = {-1,1};
          byte[] multiplier = {1,2,3,4,5,6,7,8};
          for (byte sgn : sign)
          {
            for (byte[] base : basis)
            {
              for (byte i=0; i<8; i++)
              {
                byte alpha = (byte) (multiplier[i]*sgn);
                byte[] move = multiply_list(base, alpha);
                System.out.print(move[0] + ", "+ move[1]);
                byte[] out0 = add_lists(coords, move);
                System.out.println(" ... " + out0[0] + ", "+ out0[1]);
                if (in_board_space(out0))
                {
                  //byte[][] mv = {coords, out0};
                  out.add(out0);
                }
              }
            }
          }
          break;
        case 'r':
          byte[][] basis1 = {{1,0},{0,1}};
          byte[] sign1 = {-1,1};
          byte[] multiplier1 = {1,2,3,4,5,6,7,8};
          for (byte sgn : sign1)
          {
            for (byte[] base : basis1)
            {
              for (byte i=0; i<8; i++)
              {
                byte alpha = (byte) (multiplier1[i]*sgn);
                byte[] move = multiply_list(base, alpha);
                System.out.print(move[0] + ", "+ move[1]);
                byte[] out0 = add_lists(coords, move);
                System.out.println(" ... " + out0[0] + ", "+ out0[1]);
                if (in_board_space(out0))
                {
                  //byte[][] mv = {coords, out0};
                  out.add(out0);
                }
              }
            }
          }
          break;
      }// end switch statement
    }

    return out;
  }
  private static boolean in_board_space(byte[] out0)
  {
    if (out0[0]<8 && out0[0]>-1 && out0[1] < 8 && out0[1] >-1){return true;}
    else {return false;}
  }
  
  private static char[][][] construct_board(String lst)
  {
    char[][][] out = new char[8][8][2];
    String[] rows = lst.split("=");
    for (byte i=0; i<8; i++)
    {
      //System.out.println(row);
      char[][] row_l = new char[8][2];
      String[] pieces = rows[i].split("_");
      for (byte j=0; j<8; j++)
      {
        for (byte k=0; k<2; k++)
        {
          row_l[j][k] = pieces[j].charAt(k);
        }
        
        //System.out.print(piece+"\n");
      }
      out[i] = row_l;
    }
    return out;
  }
  private static byte[] add_lists(byte[] l1, byte[] l2)
  {
    int size = l1.length;
    byte[] out = new byte[size];
    for (int i=0; i<size; i++)
    {
      out[i] = (byte) (l1[i] + l2[i]);
    }
    return out;
  }
  private static byte[] multiply_list(byte[] l1, byte alpha)
  {
    int size = l1.length;
    byte[] out = new byte[size];
    for (int i=0; i<size; i++)
    {
      out[i] = (byte) (l1[i] * alpha);
    }
    return out;
  }
  private static void print_board(char[][][] board)
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
