import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import operations;


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

  

  public static void main(String[] args) {

    char[][][] board = operations.construct_board(args[0]);
    //print_board(board);
    char clr = args[1].charAt(0);
    byte N = (byte) (Integer.parseInt(args[2]));
    boolean check = false;
    boolean legal=false;
    char specialty_piece = 'k';

    if (args[3].equals("list_all"))
      check=true;
    else if (args[3].equals("legal"))
      legal=true;
    
    if (args[4].equals("pawn"))
      specialty_piece = 'p';
    else if (args[4].equals("knight"))
      specialty_piece = 'n';

    byte extra_moves_ = (byte) (Integer.parseInt(args[5]));
        
      
    

    
    ArrayList<byte[][]> mvs = recursive_ai_enhanced(board, clr, N, extra_moves_ ,specialty_piece);
    if (legal) // don't allow illegal moves: 
      mvs = filter_illegal_moves(board, mvs); 
    
    if (mvs.size()==0) // if none of those moves were legal, find a group that are
    {
      mvs = moves.get_moves(board, clr);
      mvs = filter_illegal_moves(board, moves.get_moves(board, clr));
    }
    
    if (check)
    {
      for (byte[][] mv0 : mvs)
      {
        System.out.println(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
    }
    else
    {
      if (mvs.size()>0)
      {
        Random rando = new Random();
        int rand_i = rando.nextInt(mvs.size());
        byte[][] mv0 = mvs.get(rand_i);
        System.out.print(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
      else System.out.println("STALEMATE");
    }
  }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
/**
private static byte[][] choose_aggresive_moves(ArrayList<byte[][]> moves)
{

}
*/
private static ArrayList<byte[][]> filter_illegal_moves(char[][][] board, ArrayList<byte[][]> moves)
{
  ArrayList<byte[][]> out = new ArrayList<byte[][]>();
  char color = board[moves.get(0)[0][0]][moves.get(0)[0][1]][0];
  for (byte[][] mv0 : moves)
    if (false==in_check(operations.execute_move(board, mv0[0], mv0[1]), color))
      out.add(mv0);
  return out;
}
private static boolean in_check(char[][][] board, char color)
{
  boolean out = false;
  char other_color;
  if (color=='b') other_color = 'w';
  else if (color == 'w') other_color = 'b';
  else return true;
  byte[] king_coords = new byte[2];
  for (byte i=0; i<8; i++)
  {
    for (byte j=0; j<8; j++)
    {
      if (board[i][j][0] == color && board[i][j][1] == 'k')
      {
        king_coords[0] = i; king_coords[1] = j;
      }
    }
  }
  ArrayList<byte[][]> mvs = moves.get_moves(board, other_color);
  for (byte[][] mv : mvs)
  {
    if (mv[1][0] == king_coords[0] && mv[1][1] == king_coords[1])
    {
      out = true;
      break;
    }
  }
  return out;
}

private static ArrayList<byte[][]> recursive_ai_enhanced(
  char[][][] board1, 
  char color, 
  byte N,
  byte extra_moves,
  char special_piece)
{
  char new_move_color;
  if (color=='w'){ new_move_color = 'b'; }
  else { new_move_color = 'w'; }
  // get initial black and white lists:
  ArrayList<byte[]> my_coords = new ArrayList<byte[]>();
  ArrayList<byte[]> enemy_coords = new ArrayList<byte[]>();
  for (byte i=0; i<8; i++)
  {
    for (byte j=0; j<8; j++)
    {
      byte[] coord = {i, j};
      if (color == board1[i][j][0])
        my_coords.add(coord);
      else if (new_move_color == board1[i][j][0])
        enemy_coords.add(coord);
    }
  }

  ArrayList<byte[][]> mvs = moves.get_moves(board1, my_coords);
  byte[] scores = new byte[mvs.size()];

  
  byte wcs = -127; // start as the worst possible
  byte current_board_score = board_score(board1, color);
  byte negative_next_board_score;
  byte updated_extra_moves;
  byte updated_N;
  for (int i=0; i<mvs.size(); i++)
  {
    byte[][] move = mvs.get(i);
    ArrayList<ArrayList<byte[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
    ArrayList<byte[]> upd_my_coords = updated_coords.get(0); 
    ArrayList<byte[]> upd_enemy_coords = updated_coords.get(1); 
    updated_extra_moves = extra_moves; // reset for every move
    updated_N = N; // reset for every move
    char attacking_piece = board1[move[0][0]][move[0][1]][1];
    char attacked_piece = board1[move[1][0]][move[1][1]][1]; 
    negative_next_board_score = (byte) (-current_board_score + 
                              piece_value(attacked_piece));
    if (N>0)
    { 
      if (attacking_piece == special_piece && extra_moves>0)
      { // if the piece you are using to attack is your specialty, look further
        updated_N += 1;
        updated_extra_moves -= 1;
      }
      //ArrayList<byte[]> upd_my_coords = new ArrayList<byte[]>(my_coords);
      //ArrayList<byte[]> upd_enemy_coords = new ArrayList<byte[]>(enemy_coords);
      char[][][] board2 = operations.execute_move(board1, move[0], move[1]);        
      scores[i] = get_min_or_max_enhanced(
        board2, 
        new_move_color, 
        updated_N, 
        wcs, 
        negative_next_board_score,
        special_piece,
        updated_extra_moves,
        upd_enemy_coords,
        upd_my_coords);
      if (filter_illegal_moves(board2, moves.get_moves(board2, new_move_color)).size()==0 && 
      false==in_check(board2, new_move_color))
        scores[i]=0;
     
    }
    else
    {
      scores[i] = board_score(operations.execute_move(board1, move[0],move[1]), color);
    }
    if (scores[i] > wcs)
    {
      wcs = scores[i];
    }
  }
  byte max_score = scores[operations.max_index(scores)];
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
/**
private static byte get_min_or_max_enhanced(
        char[][][] board1, 
        char move_color,
        byte n_left,
        byte wcs,
        byte current_board_score,
        char special_piece,
        byte extra_moves)
  /** The idea is as follows: suppose white is thinking ahead and comes up with a
   * move in which he can force a gain of 5 points. He is now considering his next
   * possible move but discovers that if he does that black can then force that white
   *  only gets 4 instead of 5 points. White should then abandon that move 
   * and not even calculate the rest of the possibilities
   * In each layer this is implemented it should save about half of the computations. 
  
  {
    byte out=0;
    ArrayList<byte[][]> mvs = moves.get_moves(board1, move_color);
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
        byte updated_extra_moves;
        byte updated_N;
        for (int i=0; i<mvs.size(); i++)
        { 
          updated_extra_moves = extra_moves; // reset for every move
          updated_N = n_left; // reset for every move
          byte[][] move = mvs.get(i);
          char attacking_piece = board1[move[0][0]][move[0][1]][1];
          char attacked_piece = board1[move[1][0]][move[1][1]][1];
          if (attacking_piece == special_piece && extra_moves>0)
          { // if the piece you are attacking is your specialty, look further
            updated_N += 1;
            updated_extra_moves -= 1;
          }
          negative_next_board_score = (byte) (-current_board_score + 
                      piece_value(board1[move[1][0]][move[1][1]][1]));
          if (attacked_piece == 'k')
            scores[i] = (byte) (100 + n_left);//127; // and don't go deeper
          else 
            scores[i] = get_min_or_max_enhanced(
            operations.execute_move(board1, move[0], move[1]), 
            new_move_color, 
            (byte) (updated_N-1), 
            new_wcs, 
            negative_next_board_score,
            special_piece,
            updated_extra_moves);
          if (new_wcs < scores[i]) // if there's a better path,
          {// there is a new worst case scenario
            new_wcs = scores[i];
          }
          if (scores[i] > -wcs)
          {
            break;
          }
          //board_score(operations.execute_move(board1,move[0], move[1]), move_color);
        }
        out = scores[operations.max_index(scores)];
        
      }
      else
      {
        byte[] scores = new byte[mvs.size()];
        for (int i=0; i<mvs.size(); i++)
        {
          byte[][] move = mvs.get(i);
          if (board1[move[1][0]][move[1][1]][1] == 'k')
            scores[i] = (byte) (100 + n_left); // and don't go deeper
          else 
            scores[i] = (byte) (-current_board_score + piece_value(board1[move[1][0]][move[1][1]][1]));
          if (scores[i] > -wcs)
            {
              break;
            }
            
        }
        //System.out.println(scores[operations.max_index(scores)]);
        out = scores[operations.max_index(scores)];
      }
    }
    else {out=0;}
    return (byte) (-out);
  }
*/
  private static byte get_min_or_max_enhanced(
    char[][][] board1, 
    char move_color,
    byte n_left,
    byte wcs,
    byte current_board_score,
    char special_piece,
    byte extra_moves,
    ArrayList<byte[]> my_coords,
    ArrayList<byte[]> enemy_coords)
/** The idea is as follows: suppose white is thinking ahead and comes up with a
* move in which he can force a gain of 5 points. He is now considering his next
* possible move but discovers that if he does that black can then force that white
*  only gets 4 instead of 5 points. White should then abandon that move 
* and not even calculate the rest of the possibilities
* In each layer this is implemented it should save about half of the computations. 
*/  
{
byte out=0;
//calculate moves using known coordinates to increase speed
ArrayList<byte[][]> mvs = moves.get_moves(board1, my_coords);
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
    byte updated_extra_moves;
    byte updated_N;
    for (int i=0; i<mvs.size(); i++)
    { 
      updated_extra_moves = extra_moves; // reset for every move
      updated_N = n_left; // reset for every move
      byte[][] move = mvs.get(i);
      // get updated coordinates for next recursive move call
      ArrayList<ArrayList<byte[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
      ArrayList<byte[]> upd_my_coords = updated_coords.get(0); 
      ArrayList<byte[]> upd_enemy_coords = updated_coords.get(1); 
      char attacking_piece = board1[move[0][0]][move[0][1]][1];
      char attacked_piece = board1[move[1][0]][move[1][1]][1];
      if (attacking_piece == special_piece && extra_moves>0)
      { // if the piece you are attacking is your specialty, look further
        updated_N += 1;
        updated_extra_moves -= 1;
      }
      negative_next_board_score = (byte) (-current_board_score + 
                  piece_value(board1[move[1][0]][move[1][1]][1]));
      if (attacked_piece == 'k')
        scores[i] = (byte) (100 + n_left);//127; // and don't go deeper
      else 
        scores[i] = get_min_or_max_enhanced(
        operations.execute_move(board1, move[0], move[1]), 
        new_move_color, 
        (byte) (updated_N-1), 
        new_wcs, 
        negative_next_board_score,
        special_piece,
        updated_extra_moves,
        upd_enemy_coords,
        upd_my_coords);
      if (new_wcs < scores[i]) // if there's a better path,
      {// there is a new worst case scenario
        new_wcs = scores[i];
      }
      if (scores[i] > -wcs)
      {
        break;
      }
      //board_score(operations.execute_move(board1,move[0], move[1]), move_color);
    }
    out = scores[operations.max_index(scores)];
    
  }
  else
  {
    byte[] scores = new byte[mvs.size()];
    for (int i=0; i<mvs.size(); i++)
    {
      byte[][] move = mvs.get(i);
      if (board1[move[1][0]][move[1][1]][1] == 'k')
        scores[i] = (byte) (100 + n_left); // and don't go deeper
      else 
        scores[i] = (byte) (-current_board_score + piece_value(board1[move[1][0]][move[1][1]][1]));
      if (scores[i] > -wcs)
        {
          break;
        }
        
    }
    //System.out.println(scores[operations.max_index(scores)]);
    out = scores[operations.max_index(scores)];
  }
}
else {out=0;}
return (byte) (-out);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
/**
  private static byte[][] aggressive_ai(char[][][] board1, char color)
  {
   
    ArrayList<byte[][]> mvs = moves.get_moves(board1, color);
    //char[][][] board2 = new char[8][8][2];
    byte[] scores = new byte[mvs.size()];
    for (int i=0; i<mvs.size(); i++)
    {
      byte[][] move = mvs.get(i);
      scores[i] = board_score(operations.execute_move(board1,move[0], move[1]), color);
    }
    
    return mvs.get(operations.max_index(scores));
  }
  **/
  

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

}
