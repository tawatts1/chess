import tkinter as tk
from Board import Board
import subprocess
import time

#br_00_bb_bq_bk_bb_bn_br=00_00_00_00_00_00_00_00=00_00_bn_00_00_00_00_00=00_00_00_00_00_00_00_00=00_00_00_wp_00_00_00_00=00_00_00_00_wp_00_00_00=wp_wp_wp_00_00_wp_wp_wp=wr_wn_wb_wq_wk_wb_wn_wr"
def java_board_string(board):
    out = ''
    for i in range(8):
        for j in range(8):
            occ = board.sq_dict[(i,j)].occupant
            if occ is None:
                occ = '00'
            out += occ
            if not i+j==14:
                if not j==7:
                    out+='_'
                else:
                    out+='='
    return out
def java_ai(board, color, N=4, 
            special_option='legal',
            specialty = 'knight', 
            extra_moves = 0,
            file = 'next_move'):
    t0 = time.time()
    string = java_board_string(board)
    #print(string)
    cmd = f"java {file} {string} {color} {N} {special_option} {specialty} {extra_moves}"
    print(cmd)
    cmd = cmd.split()
    #if special_option:
    #    cmd.append(special_option)
    move =  subprocess.run(
                        cmd,
                        check=True, 
                        stdout=subprocess.PIPE).stdout.decode('ascii')
    move = [int(x) for x in move.split(',')]
    print('time: ', time.time()-t0)
    return (move[0], move[1]), (move[2], move[3])



if __name__ == "__main__":
    '''
    from Virtual_board import gen_standard_board
    vb = gen_standard_board()
    print(java_ai(vb, 'b'))
    '''
    from functools import partial
    root = tk.Tk()
    my_ai = partial(java_ai, **{'N':4,'extra_moves':0})
    b1 = Board(root, ai = my_ai)
    root.mainloop()