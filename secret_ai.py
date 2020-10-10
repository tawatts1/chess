#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep 30 22:40:28 2020

@author: ted
"""
import tkinter as tk
from random import choice
from Board import Board
from moves0 import in_checkmate, in_stalemate, pre_score
from numpy import inf
from time import time

def board_score(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    sign = {color:1, enemy_color:-1}
    value = {'p':1,'b':3,'n':3,'r':5,'q':9,'k':0}
    score = 0
    '''
    if in_checkmate(board, color):
        score = -inf
    elif in_checkmate(board, enemy_color):
        score = inf
    
    if in_stalemate(board):
        score = 0
    '''
    ps = pre_score(board, color)
    if ps is not None:
        score = ps
    else:
        for sq in board.sq_dict.values():
            occ = sq.occupant
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
    for c1, c2, board1 in board.get_next_boards(color):
        wcs = inf # if the worst is worse than the lower limit, don't go
        scores_list = []
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            score_2 = board_score(board2, color)
            scores_list.append(score_2)
            if score_2 < wcs:
                wcs = score_2
            if wcs < lower_limit:
                break
        if wcs > lower_limit:
            best_move = (c1,c2)
            lower_limit = wcs
    return best_move
'''
def two_step_random(board, color):
    enemy_color = {'b':'w','w':'b'}[color]
    #one_step_boards = board.get_next_boards(color = color)
    lower_limit = -inf
    best_move = None
    best_moves = []
    for c1, c2, board1 in board.get_next_boards(color):
        wcs = inf # if the worst is worse than the lower limit, don't go
        scores_list = []
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            score_2 = board_score(board2, color)
            scores_list.append(score_2)
            if score_2 < wcs:
                wcs = score_2
            if wcs < lower_limit:
                break
        if wcs == lower_limit:
            best_moves.append((c1,c2))
        if wcs > lower_limit:
            best_move = (c1,c2)
            best_moves = [best_move]
            lower_limit = wcs
    return choice(best_moves)
def recursive_manager(board, color):
    t0 = time()
    out = two_step_recursive(board, color, -inf, 1)
    print('time: ', time()-t0)
    print(out)
    return choice(out[0])
def two_step_recursive(board, color, lower_limit, pairs_left):
    enemy_color = {'b':'w','w':'b'}[color]
    pl = pairs_left-1
    LL = lower_limit
    out = []
    if pl == 0:
        for c1, c2, board1 in board.get_next_boards(color):
            wcs = inf
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
            
                
'''
def four_step(board, color):
    t0 = time()
    enemy_color = {'b':'w','w':'b'}[color]
    lower_limit = -inf
    best_move = None
    best_moves = []
    for c1, c2, board1 in board.get_next_boards(color):
        print('partial: ',time()-t0)
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            for c5,c6,board3 in board2.get_next_boards(color):
                scores_f = []
                for c7,c8, board4 in board3.get_next_boards(enemy_color):
                    score = board_score(board4,color)
                    scores_f.append(score)
                wcs = min(scores_f)
                if wcs > lower_limit:
                    lower_limit = wcs
                    best_moves = [(c1,c2)]
                elif wcs == lower_limit and not (c1,c2) in best_moves:
                    best_moves.append((c1,c2))
    print(time()-t0)
    return choice(best_moves)
'''
def four_step_enhanced(board, color):
    t0 = time()
    enemy_color = {'b':'w','w':'b'}[color]
    lower_limit = -inf
    best_move = None
    best_moves = []
    bs0 = board_score(board, color)
    for c1, c2, board1 in board.get_next_boards(color):
        bs1 = board_score(board1)
        print('partial: ',time()-t0)
        for c3, c4,board2 in board1.get_next_boards(enemy_color):
            bs2 = board_score(board2)
            if bs2 - bs0 <= -3: 
                break
            for c5,c6,board3 in board2.get_next_boards(color):
                scores_f = []
                for c7,c8, board4 in board3.get_next_boards(enemy_color):
                    score = board_score(board4,color)
                    scores_f.append(score)
                wcs = min(scores_f)
                if wcs > lower_limit:
                    lower_limit = wcs
                    best_moves = [(c1,c2)]
                elif wcs == lower_limit and not (c1,c2) in best_moves:
                    best_moves.append((c1,c2))
    print(time()-t0)
    return choice(best_moves)


def get_occ_color(board, coords):
    occ = board.sq_dict[tuple(coords)].occupant
    if occ is None:
        return None
    else:
        return occ[0]
def agressive_ai(board, color):
    out = []
    mvs = board.get_next_boards(color = color)
    for c1, c2, new_board in mvs:
        if get_occ_color(board, c2) == {'b':'w','w':'b'}[color]:
            out.append([c1,c2,new_board])
    if len(out) == 0:
        out = mvs
    c3, c4, new_board1 = choice(out)    
    return c3, c4

if __name__ == '__main__':
    
    root = tk.Tk()
    #root.protocol("WM_DELETE_WINDOW", quit_window())
    b1 = Board(root, ai = recursive_manager)
    root.mainloop()


