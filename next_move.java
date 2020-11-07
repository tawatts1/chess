import java.util.ArrayList;
import java.util.Random;

/** 
 * "br_00_bb_bq_bk_bb_bn_br=bp_bp_bp_bp_bp_bp_bp_bp=00_00_bn_00_00_00_00_00=00_00_00_00_00_00_00_00=00_00_00_wp_00_00_00_00=00_00_00_00_wp_00_00_00=wp_wp_wp_00_00_wp_wp_wp=wr_wn_wb_wq_wk_wb_wn_wr"
 * "br_00_bb_bq_bk_bb_bn_br=00_00_00_00_00_00_00_00=00_00_bn_00_00_00_00_00=00_00_00_00_00_00_00_00=00_00_00_wp_00_00_00_00=00_00_00_00_wp_00_00_00=wp_wp_wp_00_00_wp_wp_wp=wr_wn_wb_wq_wk_wb_wn_wr"

 * 
 * 
 * **/

public class next_move
{
  public static void main(String[] args)
  {
    
    char[][][] board = construct_board(args[0]);
    
    char clr = 'b';
    
    byte[][] mv0 = aggressive_ai(board, clr);
    
    System.out.print(mv0[0][0] + ","  
                    + mv0[0][1] + ","
                    + mv0[1][0] + "," 
                    + mv0[1][1]);
    
  }

  private static byte[][] aggressive_ai(char[][][] board1, char color)
  {
    //ArrayList<char[][][]> boards = get_next_boards(board1, color);
    
    ArrayList<byte[][]> mvs = get_moves(board1, color);
    char[][][] board2 = new char[8][8][2];
    byte[] scores = new byte[mvs.size()];
    for (int i=0; i<mvs.size(); i++)
    {
      byte[][] move = mvs.get(i);
      scores[i] = board_score(execute_move(board1,move[0], move[1]), color);
    }
    
    return mvs.get(max_index(scores));
  }
  private static int max_index(byte[] arr)
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
/**
  private static ArrayList<char[][][]> get_next_boards(char[][][] board1, char color)
  {
    ArrayList<char[][][]> out = new ArrayList<char[][][]>(); 
    ArrayList<byte[][]> mvs = get_moves(board1, color);
    for (byte[][] mv : mvs)
    {
      char[][][] board2 = execute_move(board1, mv[0], mv[1]);
      out.add(board2);
    }
    return out;
  }
  **/
  private static char[][][] execute_move(char[][][] board, byte[] c1, byte[] c2)
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
        board1[i] = board[i];//.clone();//.clone(); // no clone
      }
      
    }
    char[] empty = {'0','0'};
    char[] piece = board1[c1[0]][c1[1]];
    board1[c2[0]][c2[1]] = piece;
    board1[c1[0]][c1[1]] = empty;
    return board1;
  }
  private static ArrayList<byte[][]> get_moves(char[][][] board, char color)
  { // returns all moves for a certain color
    ArrayList<byte[][]> out = new ArrayList<byte[][]>();
    //byte[][] mv = new byte[2][2];
    for (byte i=0; i<8; i++)
    {
      for (byte j=0; j<8; j++)
      {
        if (board[i][j][0] == color)
        {
          byte[] c1 = {i,j};
          for (byte[] c2 : moves(board, c1, color))
          {
            byte[][] mv = {c1,c2};
            out.add(mv);
          }
          
        }
      }
    }
    return out;
  }
  private static ArrayList<byte[]> moves(char[][][] board, byte[] coords, char color)
  {return moves_pre_check(board, coords, color);}
  private static ArrayList<byte[]> moves_pre_check(char[][][] board, byte[] coords, char color)
  {
    ArrayList<byte[]> out = new ArrayList<byte[]>();
    char piece = board[coords[0]][coords[1]][1];
    char piece_color = board[coords[0]][coords[1]][0];
    if (piece_color==color)
    {
      byte ff; // friendly fire
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
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff != 1)
              {
                out.add(out0);
              }
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
                byte[] out0 = add_lists(coords, move);
                if (in_board_space(out0))
                {
                  ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                  if (ff==0){out.add(out0);}
                  else if (ff==1){break;}
                  else 
                  {
                    out.add(out0);
                    break;
                  }
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
                byte[] out0 = add_lists(coords, move);
                if (in_board_space(out0))
                {
                  ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                  if (ff==0){out.add(out0);}
                  else if (ff==1){break;}
                  else 
                  {
                    out.add(out0);
                    break;
                  }
                }
              }
            }
          }
          break;
        case 'q':
          byte[][] basis2 = {{1,0},{0,1}, {-1,1},{1,1}};
            byte[] sign2 = {-1,1};
            byte[] multiplier2 = {1,2,3,4,5,6,7,8};
            for (byte sgn : sign2)
            {
              for (byte[] base : basis2)
              {
                for (byte i=0; i<8; i++)
                {
                  byte alpha = (byte) (multiplier2[i]*sgn);
                  byte[] move = multiply_list(base, alpha);
                  byte[] out0 = add_lists(coords, move);
                  if (in_board_space(out0))
                  {
                    ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                    if (ff==0){out.add(out0);}
                    else if (ff==1){break;}
                    else 
                    {
                      out.add(out0);
                      break;
                    }
                  }
                }
              }
            }
            break;

        case 'k':
          byte[][] basis3 = {{1,0},{0,1}, {-1,1},{1,1}};
          byte[] sign3 = {-1,1};
          //byte[] multiplier3 = {1};
          for (byte sgn : sign3)
          {
            for (byte[] base : basis3)
            {
            
              //byte alpha = (byte) (multiplier3[i]*sgn);
              byte[] move = multiply_list(base, sgn);
              byte[] out0 = add_lists(coords, move);
              if (in_board_space(out0))
              {
                ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
                if (ff != 1)
                {
                  out.add(out0);
                }
              }
            
            }
          }
          break;
        case 'p':
          byte sgn = get_pawn_sign(piece_color);
          byte[] mv0 = {sgn, 0};
          byte[][] attacks = {{sgn, 1}, {sgn, -1}};
          byte[] out0 = add_lists(coords, mv0);
          if (in_board_space(out0))
          {
            ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
            if (ff == 0)
            {
              out.add(out0);
            }
          }
          for (byte[] mva : attacks)
          {
            out0 = add_lists(coords, mva);
            if (in_board_space(out0))
            {
              ff = friendly_fire(piece_color, board[out0[0]][out0[1]][0]);
              if (ff == -1)
              {
                out.add(out0);
              }
            }
          }
          //out.add(out0);
          break;
        
      }// end switch statement
    }

    return out;
  }

  

  private static byte board_score(char[][][] board, char color)
  {
    byte out = 0;
    byte val = 0;
    char pp;
    for (byte i=0; i<8; i++)
    {
      for (byte j=0; j<8; j++)
      {
        pp = board[i][j][1];
        if (pp!='0')
        {
          val = piece_value(pp);
          if (color==board[i][j][0]){out+=val;}
          else {out-=val;}
        }
      }
    }
    return out;
  }
  private static byte piece_value(char piece)
  {
    byte out=0;
    switch (piece)
    {
      case 'p' : out=1; break;
      case 'b' : 
      case 'n' : out = 3; break;
      case 'r' : out = 5; break;
      case 'q' : out = 9; break;
      case 'k' : out =27; break;
      default :  out = 0; break;
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
  private static byte get_pawn_sign(char color)
  {
    byte out = 0;
    if (color=='b'){out = 1;}
    else if (color=='w'){out = -1;}
    return out;
  }
  private static byte friendly_fire(char color0, char color1)
  {
    byte out;
    if      (color1 == '0')   
      {out = 0;}
    else if (color0 == color1)
      {out = 1;}
    else                      
      {out = -1;}
    return out;
  }

}
