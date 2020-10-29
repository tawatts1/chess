#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep 30 22:40:28 2020

@author: ted
"""
import tkinter as tk
from random import choice
from Board import Board
from Virtual_board_mem import VBoard_m
from moves_mem import pre_score
from numpy import inf
from time import time
import os
import psutil
process = psutil.Process(os.getpid())
print(process.memory_info().vms*10**-6)  # in bytes 

def board_score(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    sign = {color:1, enemy_color:-1}
    value = {'p':1,'b':3,'n':3,'r':5,'q':9,'k':100}
    score = 0
    '''
    if in_checkmate(board, color):
        score = -inf
    elif in_checkmate(board, enemy_color):
        score = inf
    
    if in_stalemate(board):
        score = 0
    '''
    ps = None#pre_score(board, color)
    if ps is not None:
        score = ps
    else:
        for i in range(8):
            for j in range(8):
                occ = board.rsq_dict[i][j]
                if occ is not None:
                    score += sign[occ[0]]*value[occ[1]]
    #print(score)
    return score
'''
def two_step(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    #one_step_boards = board.get_next_boards(color = color)
    lower_limit = -inf
    best_move = None 
    i = 0
    for c1, c2, board1 in board.get_next_boards(color):
        i +=1
        wcs = inf # if the worst is worse than the lower limit, don't go
        #scores_list = []
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            score_2 = board_score(board2, color)
            #scores_list.append(score_2)
            if score_2 < wcs:
                wcs = score_2
            if wcs < lower_limit:
                break
        else: 
            if wcs > lower_limit:
                best_move = (c1,c2)
                lower_limit = wcs
    return best_move
'''
def recursive_manager(board, color):
    global process
    print(process.memory_info().vms*10**-6)  # in bytes 
    t0 = time()
    print('color: ' + color)
    vb_m = VBoard_m(board.sq_dict, None)
    out = two_step_recursive(vb_m, color, -inf, 1)
    print('time: ', time()-t0)
    print(out)
    return choice(out[0])#[0]
def two_step_recursive(board, color, lower_limit, pairs_left):
    enemy_color = {'b':'w','w':'b'}[color]
    pl = pairs_left-1
    LL = lower_limit
    out = []
    if pl == 0:
        for c1, c2, board1 in board.get_next_boards(color):
            wcs = inf
            #print(board1)
            for c3, c4, board2 in board1.get_next_boards(enemy_color):
                score2 = board_score(board2, color)
                if score2<wcs:
                    wcs = score2
                if score2 < lower_limit:
                    break
            move = (c1,c2)
            if wcs == LL:
                if move not in out:
                    out.append(move)
            elif wcs > LL:
                out = [move]
                LL = wcs
        return out, LL
    elif pl > 0:
        for c1, c2, board1 in board.get_next_boards(color):
            wcs = inf
            for c3, c4, board2 in board1.get_next_boards(enemy_color):
                null, score2 = two_step_recursive(board2, color, LL, pl)
                if score2<wcs:
                    wcs = score2
                if score2 < LL:
                    break
            #move = (c1,c2)
            move = (c1,c2)
            if wcs == LL:
                move = (c1,c2)
                if not move in out:
                    out.append(move)
            elif wcs > LL:
                out = [move]
                LL = wcs
        return out, LL
            
def get_occ_color(board, coords):
    occ = board.rsq_dict[coords[0]][coords[1]]
    if occ is None:
        return None
    else:
        return occ[0]

def minmax_manager(board,color):
    t0 = time()
    vb_m = VBoard_m(board.sq_dict, None)
    out,score = get_min_or_max(vb_m, color, color, 3, 'max')
    print(time()-t0)
    print(score)
    return out

def get_min_or_max(board, color,move_color, n_left,minmax):
    nl = n_left - 1
    if nl == 0:
        scores = []
        moves = []
        for c1,c2,board1 in board.get_next_boards(move_color):
            scores.append(board_score(board1,color))
            moves.append((c1,c2))
        mm_val = eval(minmax+f'({scores})')
        #print(mm_val)
        return moves[scores.index(mm_val)], mm_val
    elif nl >0:
        scores = []
        moves = []
        new_minmax = {'min':'max','max':'min'}[minmax]
        new_move_color = {'w':'b','b':'w'}[move_color]
        for c1,c2,board1 in board.get_next_boards(move_color):
            sc = get_min_or_max(board1, color,new_move_color, nl, new_minmax)[1]
            scores.append(sc)
            moves.append((c1,c2))
        mm_val = eval(minmax+f'({scores})')
        return moves[scores.index(mm_val)], mm_val


if __name__ == '__main__':
    from Virtual_board import gen_standard_board
    vb = gen_standard_board()
    vb.execute_move((6,4),(5,4))
    vb.execute_move((0,1),(2,2))
    vb.execute_move((6,3),(4,3))
    print(vb)
    #print(minmax_manager(vb,'b'))




    root = tk.Tk()
    b1 = Board(root, ai = minmax_manager)
    root.mainloop()


