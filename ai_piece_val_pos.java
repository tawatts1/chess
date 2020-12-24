import java.util.ArrayList;

public class ai_piece_val_pos 
{

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

        ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
        ai_util.prioritize_moves(board1, mvs, new_move_color);
        int[] scores = new int[mvs.size()];


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
                //negative_next_board_score_position,
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
        char special_piece,
        int extra_moves,
        ArrayList<int[]> my_coords,
        ArrayList<int[]> enemy_coords)
        /** The idea is as follows: suppose white is thinking ahead and comes up with a
        * move in which he can force a gain of 5 points. He is now considering his next
        * possible move but discovers that if he does that black can then force that white
        *  only gets 4 instead of 5 points. White should then abandon that move 
        * and not even calculate the rest of the possibilities
        * In each layer this is implemented it should save about half of the computations. 
        */  
    {
        int out=0;
        char new_move_color;
        if (move_color=='w'){ new_move_color = 'b'; }
        else { new_move_color = 'w'; }
        //calculate moves using known coordinates to increase speed
        ArrayList<int[][]> mvs = moves_methods.get_moves(board1, my_coords);
        ai_util.prioritize_moves(board1, mvs, new_move_color);

        int[] scores = new int[mvs.size()];
        int new_wcs = -10000; // start as the worst possible


        if (n_left > 1)
        {
            int updated_extra_moves;
            int updated_N;
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
                if (attacked_piece == 'k')
                    scores[i] = 9000 + n_left; // and don't go deeper
                else 
                    scores[i] = minmax_position(
                        operations.execute_move(board1, move[0], move[1]), 
                        new_move_color, 
                        updated_N-1, 
                        new_wcs, 
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
                if (board1[move[1][0]][move[1][1]][1] == 'k')
                scores[i] = 9000 + n_left; // and don't go deeper
                else 
                    scores[i] = board_score_position(operations.execute_move(board1, move[0], move[1]),
                                     move_color);
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

    
}
