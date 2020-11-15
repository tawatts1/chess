import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class next_move {
/**
  public static class thread_score implements Callable<Byte> {
    char[][][] board0;
    char color;
    byte N;

    public thread_score(char[][][] board0, char color, byte N) {
      this.board0 = board0;
      this.color = color;
      this.N = N;
    }

    //@Override // not needed?
    public Byte call() throws Exception {
      return get_min_or_max(board0, color, N);
    }
  }
  **/

  private static final char[] EMPTY = { '0', '0' };
  private static final char[] BP = { 'b', 'p' };
  private static final char[] WP = { 'w', 'p' };
  private static final char[] BQ = { 'b', 'q' };
  private static final char[] WQ = { 'w', 'q' };
  private static final char[] BK = { 'b', 'k' };
  private static final char[] WK = { 'w', 'k' };
  private static final char[] BB = { 'b', 'b' };
  private static final char[] WB = { 'w', 'b' };
  private static final char[] BN = { 'b', 'n' };
  private static final char[] WN = { 'w', 'n' };
  private static final char[] BR = { 'b', 'r' };
  private static final char[] WR = { 'w', 'r' };

  public static void main(String[] args) {

    char[][][] board = construct_board(args[0]);

    char clr = args[1].charAt(0);
    byte N = (byte) (Integer.parseInt(args[2]));
    boolean check = false;
    if (args.length > 3 && args[3].equals("true"))
    {
      check = true;
    }

    
    ArrayList<byte[][]> mvs = recursive_ai_enhanced(board, clr, N);// aggressive_ai(board, clr);
    mvs = filter_illegal_moves(board, mvs);
    if (mvs.size()==0) mvs = filter_illegal_moves(board, get_moves(board, clr));
    if (check)
    {
      for (byte[][] mv0 : mvs)
      {
        System.out.println(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
    }
    else
    {
      Random rando = new Random();
      int rand_i = rando.nextInt(mvs.size());
      byte[][] mv0 = mvs.get(rand_i);
      System.out.print(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
    }
  }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
/**
private static void test_illegal_filter()
{

}
*/
private static ArrayList<byte[][]> filter_illegal_moves(char[][][] board1, ArrayList<byte[][]> mvs) 
{
  ArrayList<byte[][]> out = new ArrayList<byte[][]>();
  //get color
  char color = board1[mvs.get(0)[0][0]][mvs.get(0)[0][1]][0];
  char other_color;
  if      (color=='b')  other_color = 'w';
  else if (color=='w')  other_color = 'b';
  else return out;
  //locate king of that color
  byte[] king_coords = new byte[2];
  for (byte i=0; i<8; i++)
  {
    for (byte j=0; j<8; j++)
    {
      if (board1[i][j][0] == color && board1[i][j][1] == 'k')
      {
        king_coords[0] = i; king_coords[1] = j;
        //System.out.println("found king");
        break;
      }
    }
  }
  // admit only moves which do not put in check. 
  char[][][] board2 = new char[8][8][2];
  boolean illegal;
  for (byte[][] mv0 : mvs)
  {
    illegal = false;
    board2 = execute_move(board1, mv0[0], mv0[1]);
    ArrayList<byte[][]> mvs2 = get_moves(board2, other_color);
    for (byte[][] mv2: mvs2)
    {
      if (Arrays.equals(mv2[1], king_coords))//(king_coords[0]==mv2[1][0] && king_coords[1] == mv2[1][1])//(king_coords.equals(mv2[1]))
      {
        illegal=true;
        break;
        //out.add(mv0);
      }
    }
    if (false==illegal) out.add(mv0);
  }
  return out;
}
private static ArrayList<byte[][]> recursive_ai_enhanced(char[][][] board1, char color, byte N)
{
  ArrayList<byte[][]> mvs = get_moves(board1, color);
  byte[] scores = new byte[mvs.size()];

  char new_move_color;
  if (color=='w'){ new_move_color = 'b'; }
  else { new_move_color = 'w'; }
  byte wcs = -127; // start as the worst possible
  byte current_board_score = board_score(board1, color);
  byte negative_next_board_score;
  for (int i=0; i<mvs.size(); i++)
  {
    byte[][] move = mvs.get(i);
    negative_next_board_score = (byte) (-current_board_score + 
                              piece_value(board1[move[1][0]][move[1][1]][1]));
    if (N>0)
    {                     
      scores[i] = get_min_or_max_enhanced(
        execute_move(board1, move[0], move[1]), 
        new_move_color, N, wcs, negative_next_board_score);
    }
    else
    {
      scores[i] = board_score(execute_move(board1, move[0],move[1]), color);
    }
    if (scores[i] > wcs)
    {
      wcs = scores[i];
    }
    //board_score(execute_move(board1,move[0], move[1]), color);
  }
  byte max_score = scores[max_index(scores)];
  ArrayList<byte[][]> out = new ArrayList<byte[][]>();
  for (int i=0; i<mvs.size(); i++)
  {
    if (max_score == scores[i])
    {
      out.add(mvs.get(i));
    }
  }
  return out;
}
private static byte get_min_or_max_enhanced(
        char[][][] board1, 
        char move_color,
        byte n_left,
        byte wcs,
        byte current_board_score)
  /** The idea is as follows: suppose white is thinking ahead and comes up with a
   * move in which he can force a gain of 5 points. He is now considering his next
   * possible move but discovers that if he does that black can then force that white
   *  only gets 4 instead of 5 points. White should then abandon that move 
   * and not even calculate the rest of the possibilities
   * In each layer this is implemented it should save about half of the computations. 
 */  
  {
    byte out=0;
    ArrayList<byte[][]> mvs = get_moves(board1, move_color);
    byte new_wcs = -127; // start as the worst possible
    if (mvs.size()>0)
    {
      if (n_left > 1)
      {
        byte negative_next_board_score;
        byte[] scores = new byte[mvs.size()];
        char new_move_color;
        if (move_color=='w'){ new_move_color = 'b'; }
        else { new_move_color = 'w'; }
        for (int i=0; i<mvs.size(); i++)
        {
          byte[][] move = mvs.get(i);
          negative_next_board_score = (byte) (-current_board_score + 
                      piece_value(board1[move[1][0]][move[1][1]][1]));
          scores[i] = get_min_or_max_enhanced(
            execute_move(board1, move[0], move[1]), 
            new_move_color, (byte) (n_left-1), new_wcs, negative_next_board_score);
          if (new_wcs < scores[i]) // if there's a better path,
          {// there is a new worst case scenario
            new_wcs = scores[i];
          }
          if (scores[i] > -wcs)
          {
            break;
          }
          //board_score(execute_move(board1,move[0], move[1]), move_color);
        }
        out = scores[max_index(scores)];
        
      }
      else
      {
        byte[] scores = new byte[mvs.size()];
        for (int i=0; i<mvs.size(); i++)
        {
          byte[][] move = mvs.get(i);
          scores[i] = (byte) (-current_board_score + piece_value(board1[move[1][0]][move[1][1]][1]));
          //board_score(execute_move(board1,move[0], move[1]), move_color);
          //System.out.println(scores[i]);
          if (scores[i] > -wcs)
            {
              break;
            }
            
        }
        //System.out.println(scores[max_index(scores)]);
        out = scores[max_index(scores)];
      }
    }
    else {out=0;}
    return (byte) (-out);
  }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
/**
  private static ArrayList<byte[][]> recursive_ai(char[][][] board1, char color, byte N) {
    //
    ArrayList<byte[][]> mvs = get_moves(board1, color);
    byte[] scores = new byte[mvs.size()];

    ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(3);// !!!
    List<Callable<Byte>> callableTasks = new ArrayList<>();

    char new_move_color;
    if (color == 'w') {
      new_move_color = 'b';
    } else {
      new_move_color = 'w';
    }

    for (int i = 0; i < mvs.size(); i++) {
      // add all scoring methods after one move to a collection
      byte[][] move = mvs.get(i);
      char[][][] board2 = execute_move(board1, move[0], move[1]);
      callableTasks.add(new thread_score(board2, new_move_color, N));
    }
    List<Future<Byte>> result = null;
    try {
      result = executor.invokeAll(callableTasks);
      executor.shutdown();
      for (int i=0; i<scores.length; i++) 
      {
        scores[i] = result.get(i).get();
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    byte max_score = scores[max_index(scores)];
    ArrayList<byte[][]> out = new ArrayList<byte[][]>();
    for (int i=0; i<mvs.size(); i++)
    {
      if (max_score == scores[i])
      {
        out.add(mvs.get(i));
      }
    }
    return out;
  }
  private static byte get_min_or_max(
          char[][][] board1, 
          char move_color,
          byte n_left)
    {
      byte out=0;
      ArrayList<byte[][]> mvs = get_moves(board1, move_color);
      if (mvs.size()>0)
      {
        if (n_left > 1)
        {
            byte[] scores = new byte[mvs.size()];
            char new_move_color;
            if (move_color=='w'){ new_move_color = 'b'; }
            else { new_move_color = 'w'; }
            for (int i=0; i<mvs.size(); i++)
            {
              byte[][] move = mvs.get(i);
              scores[i] = get_min_or_max(
                execute_move(board1, move[0], move[1]), new_move_color, (byte) (n_left-1));
            }
            out = scores[max_index(scores)];
          
        }
        else
        {
          byte[] scores = new byte[mvs.size()];
          for (int i=0; i<mvs.size(); i++)
          {
            byte[][] move = mvs.get(i);
            scores[i] = board_score(execute_move(board1,move[0], move[1]), move_color);
          }
          
          out = scores[max_index(scores)];
        }
      }
      else {out=0;}
      return (byte) (-out);
    }
  private static byte[][] aggressive_ai(char[][][] board1, char color)
  {
   
    ArrayList<byte[][]> mvs = get_moves(board1, color);
    //char[][][] board2 = new char[8][8][2];
    byte[] scores = new byte[mvs.size()];
    for (int i=0; i<mvs.size(); i++)
    {
      byte[][] move = mvs.get(i);
      scores[i] = board_score(execute_move(board1,move[0], move[1]), color);
    }
    
    return mvs.get(max_index(scores));
  }
  **/
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
        board1[i] = board[i]; // no clone
      }
      
    }
    char[] piece = board1[c1[0]][c1[1]];
    board1[c2[0]][c2[1]] = piece;
    board1[c1[0]][c1[1]] = EMPTY; 
    return board1;
  }
  private static ArrayList<byte[][]> get_moves(char[][][] board, char color)
  { // returns all moves for a certain color
    ArrayList<byte[][]> out = new ArrayList<byte[][]>();
    //byte[][] mv = new byte[2][2];
    byte[] c1 = new byte[2];
    byte[] c2 = new byte[2];
    //byte[][] mv = new byte[2][2];
    ArrayList<byte[]> piece_moves = new ArrayList<byte[]>();
    
    for (byte i=0; i<8; i++)
    {
      for (byte j=0; j<8; j++)
      {
        if (board[i][j][0] == color)
        {
          c1[0] = i; c1[1] = j;
          piece_moves = moves(board, c1, color);
          for (int k=0; k<piece_moves.size(); k++)
          {
            c2 = piece_moves.get(k);
            
            out.add(make_copy(c1,c2));
          }
          
        }
      }
    }
    return out;
  }
  private static byte[][] make_copy(byte[] c1, byte[] c2)
  {
    byte[][] out = new byte[2][2];
    out[0][0] = c1[0]; out[0][1] = c1[1];
    out[1][0] = c2[0]; out[1][1] = c2[1];
    return out;
  }
  private static ArrayList<byte[]> moves(char[][][] board, byte[] coords, char color)
  {
    ArrayList<byte[]> dirty_moves = moves_pre_check(board, coords, color);
     
    return dirty_moves;
  }
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
