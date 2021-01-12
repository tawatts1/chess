
import java.util.ArrayList;
import java.util.Random;

public class next_move 
{


public static void main(String[] args) throws Exception {
  //     board_string, color, N, legal/list_all, specialty, specialty_num, scoring
    char[][][] board = operations.construct_board(args[0]);
    //operations.print_board(board);
    char clr = args[1].charAt(0);
    int N = Integer.parseInt(args[2]);
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
    else if (args[4].equals("queen"))
      specialty_piece = 'q';
    
    int extra_moves_ = Integer.parseInt(args[5]);
    ArrayList<int[][]> mvs = new ArrayList<int[][]>(); 
    switch (args[6])
    {
    case "pos" : 
      mvs = 
          ai_piece_val_pos.recursive_score_w_position(board, clr, N, extra_moves_ ,specialty_piece);
      break;
    case "pos2" : 
      mvs = 
          ai_piece_val_pos2.recursive_score_w_position(board, clr, N, extra_moves_, specialty_piece);
      break;
    default : throw new Exception("Not implemented");
    }
    

    
    
    if (legal) // don't allow illegal moves: 
      mvs = moves_methods.filter_illegal_moves(board, mvs); 
    
    if (mvs.size()==0) // if none of those moves were legal, find a group that are
    {
      mvs = moves_methods.get_moves(board, clr);
      mvs = moves_methods.filter_illegal_moves(board, mvs);
    }
    
    if (check)
    {
      for (int[][] mv0 : mvs)
      {
        System.out.println(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
    }
    else
    {
      if (mvs.size()>0)
      {
        //System.out.println(args[6]);
        
        Random rando = new Random();
        int rand_i = rando.nextInt(mvs.size());
        int[][] mv0 = mvs.get(rand_i);
        System.out.print(mv0[0][0] + "," + mv0[0][1] + "," + mv0[1][0] + "," + mv0[1][1]);
      }
      else System.out.println("STALEMATE");
    }
  }
}
