import java.util.ArrayList;

public class ai_piece_val_pos 
{
    public static void main(String[] args)
    {
        char[][][] board = new char[8][8][2];
        for (int i=0; i<8; i++) for (int j=0; j<8; j++)
        {
            board[i][j][0] = '0'; board[i][j][1] = '0';
        }
        board[0][0][0] = 'b'; board[0][0][1] = 'k';
        //board[0][4][0] = 'b'; board[0][4][1] = 'n';
        board[1][4][0] = 'b'; board[1][4][1] = 'n';
        
        board[7][5][0] = 'w'; board[7][5][1] = 'k';
        board[7][4][0] = 'w'; board[7][4][1] = 'n';
        board[6][4][0] = 'w'; board[6][4][1] = 'n';

        operations.print_board(board);

        recursive_score_w_position(board, 'b', 3, 0, 'p');
    }
    public static ArrayList<int[][]> recursive_score_w_position(
        char[][][] board1, 
        char color, 
        int N,
        int extra_moves,
        char special_piece)
    {
        // get enemy color
        char new_move_color;
        if (color=='w'){ new_move_color = 'b'; }
        else { new_move_color = 'w'; }

        // get initial black and white lists:
        ArrayList<int[]> my_coords = new ArrayList<int[]>();
        ArrayList<int[]> enemy_coords = new ArrayList<int[]>();
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                int[] coord = {i, j};
                if (color == board1[i][j][0])
                my_coords.add(coord);
                else if (new_move_color == board1[i][j][0])
                enemy_coords.add(coord);
            }
        }
        int current_board_score = board_score_position(board1, color);
        ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
        //calculate scores and sort moves
        ArrayList<Integer> next_board_scores = sort_moves(board1,current_board_score, mvs);
        //ai_util.prioritize_moves(board1, mvs, new_move_color);
        int[] scores = new int[mvs.size()];

        int negative_next_board_score;
        
        int wcs = -10000; // start as the worst possible
        int updated_extra_moves;
        int updated_N;
        for (int i=0; i<mvs.size(); i++)
        {
            int[][] move = mvs.get(i);
            ArrayList<ArrayList<int[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
            ArrayList<int[]> upd_my_coords = updated_coords.get(0); 
            ArrayList<int[]> upd_enemy_coords = updated_coords.get(1); 
            updated_extra_moves = extra_moves; // reset for every move
            updated_N = N; // reset for every move
            char attacking_piece = board1[move[0][0]][move[0][1]][1];
            negative_next_board_score = -next_board_scores.get(i);
            if (N>0)
            { 
                if (attacking_piece == special_piece && extra_moves>0)
                { // if the piece you are using to attack is your specialty, look further
                    updated_N += 1;
                    updated_extra_moves -= 1;
                }
                //ArrayList<int[]> upd_my_coords = new ArrayList<int[]>(my_coords);
                //ArrayList<int[]> upd_enemy_coords = new ArrayList<int[]>(enemy_coords);
                char[][][] board2 = operations.execute_move(board1, move[0], move[1]);        
                scores[i] = minmax_position(
                board2, 
                new_move_color, 
                updated_N, 
                wcs, 
                negative_next_board_score,
                special_piece,
                updated_extra_moves,
                upd_enemy_coords,
                upd_my_coords);

                if (moves_methods.filter_illegal_moves(board2, moves_methods.get_moves(board2, new_move_color)).size()==0 && 
                            false==moves_methods.in_check(board2, new_move_color))
                    scores[i]=0;

            }
            else // if N = 0; almost never used, unless you want a super dumb ai. 
                scores[i] = board_score_position(operations.execute_move(board1, move[0],move[1]), color);
            if (scores[i] > wcs)
            {
                wcs = scores[i];
            }
        }
        int max_score = scores[operations.max_index(scores)];
        ArrayList<int[][]> out = new ArrayList<int[][]>();
        for (int i=0; i<mvs.size(); i++)
        {
            if (max_score == scores[i])
            {
            out.add(mvs.get(i));
            }
        }
        return out;
    }

    private static int minmax_position(
        char[][][] board1, 
        char move_color,
        int n_left,
        int wcs,
        int current_board_score,
        char special_piece,
        int extra_moves,
        ArrayList<int[]> my_coords,
        ArrayList<int[]> enemy_coords)
    {
        int out=0;
        char new_move_color;
        if (move_color=='w'){ new_move_color = 'b'; }
        else { new_move_color = 'w'; }
        //calculate moves using known coordinates to increase speed
        ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
        
        int[] scores = new int[mvs.size()];
        int new_wcs = -10000; // start as the worst possible


        if (n_left > 1)
        {
            int negative_next_board_score;
            int updated_extra_moves;
            int updated_N;
            //sort moves and make board-scores list:
            ai_util.prioritize_moves(board1, mvs, new_move_color);
            ArrayList<Integer> next_board_scores = sort_moves(board1,current_board_score, mvs);

            for (int i=0; i<mvs.size(); i++)
            { 
                updated_extra_moves = extra_moves; // reset for every move
                updated_N = n_left; // reset for every move
                int[][] move = mvs.get(i);
                // get updated coordinates for next recursive move call
                ArrayList<ArrayList<int[]>> updated_coords = operations.update_coords(my_coords, enemy_coords, move);
                ArrayList<int[]> upd_my_coords = updated_coords.get(0); 
                ArrayList<int[]> upd_enemy_coords = updated_coords.get(1); 
                char attacking_piece = board1[move[0][0]][move[0][1]][1];
                char attacked_piece = board1[move[1][0]][move[1][1]][1];
                if (attacking_piece == special_piece && extra_moves>0)
                { // if the piece you are attacking is your specialty, look further
                    updated_N += 1;
                    updated_extra_moves -= 1;
                }
                negative_next_board_score = - next_board_scores.get(i);
                if (attacked_piece == 'k')
                    scores[i] = 9000 + n_left; // and don't go deeper
                else 
                    scores[i] = minmax_position(
                        operations.execute_move(board1, move[0], move[1]), 
                        new_move_color, 
                        updated_N-1, 
                        new_wcs, 
                        negative_next_board_score,
                        special_piece,
                        updated_extra_moves,
                        upd_enemy_coords,
                        upd_my_coords);
                if (scores[i] > new_wcs) // if there's a better path,
                {// there is a new worst case scenario
                    new_wcs = scores[i];
                }
                if (scores[i] > -wcs)
                {
                    break;
                }
            }
        }
        else // if n_left <= 1:
        {
            for (int i=0; i<mvs.size(); i++)
            {
                int[][] move = mvs.get(i);
                char attacking_piece = board1[move[0][0]][move[0][1]][1];
                char attacked_piece = board1[move[1][0]][move[1][1]][1];
                if (board1[move[1][0]][move[1][1]][1] == 'k')
                    scores[i] = 9000 + n_left; // and don't go deeper
                else 
                    scores[i] = current_board_score + 
                    position_difference(attacking_piece, move) +
                    position_value(attacked_piece, move[1][0], move[1][1]) +
                    ai_util.piece_value(attacked_piece) ;
                    //board_score_position(operations.execute_move(board1, move[0], move[1]),move_color);
                if (scores[i] > -wcs)
                {
                    break;
                }

            }

        }
        out = scores[operations.max_index(scores)];
        return -out;
    }

    private static int board_score_position(char[][][] board, char color)
    {
        int out = 0;
        int val = 0;
        char pp;
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                pp = board[i][j][1];
                if (pp!='0')
                {
                    val = ai_util.piece_value(pp)+position_value(pp,i,j);
                    if (color==board[i][j][0]){out+=val;}
                    else {out-=val;}
                }
            }
        }
        return out;
    }

    private static int position_difference(char piece, int[][] move)
    {
        return position_value(piece, move[1][0], move[1][1]) - position_value(piece, move[0][0], move[0][1]);
    }

    private static int position_value(char piece, int i, int j)
    {
        int out = 0;
        switch (piece)
        {
            //case 'p' : out=100; break; no pawn handling
            case 'r' : 
            case 'q' : out = 14;
                if (piece == 'r') break;
            case 'b' : 
                if      (any_equal(0,7,i,j)) out = 7;
                else if (any_equal(1,6,i,j)) out = 9;
                else if (any_equal(2,5,i,j)) out = 11;
                else out = 13;
                break;
            case 'n' : 
                if (any_equal(0,7,i,j)) 
                    if (all_equal_to_something(0, 7, i, j)) 
                        out = 2;
                    else if (any_equal(1, 6, i, j))
                        out = 3;
                    else
                        out = 4;
                else if (any_equal(1, 6, i, j))
                    if (all_equal_to_something(1, 6, i, j))
                    out = 4;
                    else out = 6;
                else out = 8;
                break;
            default  : break;
        }
        return out;
    }

    private static boolean any_equal(int val1, int val2, int i, int j)
    {
        if (val1 == i || val2 == i || val1 == j || val2 == j) 
            return true;
        else return false;
    }
    
    private static boolean all_equal_to_something(int val1, int val2, int i, int j)
    {
        if ((i == val1 || i == val2) && (j==val1 || j==val2))
            return true;
        else return false;
    }
    public static ArrayList<Integer> sort_moves(char[][][] board, int score0, ArrayList<int[][]> mvs)
    {
        ArrayList<Integer> scores = new ArrayList<>();
        int[][] move = new int[2][2];

        //write first score in score array
        move = mvs.get(0);
        char attacking_piece = board[move[0][0]][move[0][1]][1];
        char attacked_piece = board[move[1][0]][move[1][1]][1];
        int score = score0 + 
            position_difference(attacking_piece, move) +
            position_value(attacked_piece, move[1][0], move[1][1]) +
            ai_util.piece_value(attacked_piece) ;
        scores.add(score);
        
        //sort mvs and write rest of scores:
        for (int i=1; i<mvs.size(); i++)
        {
            //get score
            move = mvs.remove(i);
            attacking_piece = board[move[0][0]][move[0][1]][1];
            attacked_piece = board[move[1][0]][move[1][1]][1];
            score = score0 + 
                position_difference(attacking_piece, move) +
                position_value(attacked_piece, move[1][0], move[1][1]) +
                ai_util.piece_value(attacked_piece) ;
            // do sorting things:
            for (int j=i-1; j>=0; j--)
            {
                if (score <= scores.get(j)) // sort in descending order.
                {
                    mvs.add(j+1, move);
                    scores.add(j+1, score);
                    break;
                }
                else if (j==0) // if score is the biggest yet seen, put it first
                {
                    mvs.add(0, move);
                    scores.add(0, score);
                    break;
                }
            }
        }
        return scores;
    }
    
/** 

0,2,1,1
8
time:  3.4400429725646973

1,6,2,6
1,7,0,7
0,0,0,1
1,0,2,0
2,7,3,7
1,3,2,3
48
time:  2.604520797729492

3,6,4,6
8
time:  4.012020587921143

3,6,2,5
8
time:  2.593916654586792

0,3,1,4
8
time:  0.8541738986968994

2,4,6,0
8
time:  2.831343173980713

0,4,0,5
8
time:  0.8403105735778809

1,3,2,3
8
time:  4.933335781097412



 */

}
