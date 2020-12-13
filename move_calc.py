#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 20:47:35 2020

@author: ted
"""
import numpy as np
#from Virtual_board import EMPTY
EMPTY = ['0','0']
def in_board_space(xy, a=0,b=7):
        if a <= xy[0] <= b and a <= xy[1] <= b:
            return True
        else:
            return False;
        
def friendly_fire(vb, coord0, coord_f):
    occ_0 = vb[coord0[0]][coord0[1]]
    occ_f = vb[coord_f[0]][coord_f[1]]
    if occ_f == EMPTY:
        ans = 0
    else:
        if occ_0[0] == occ_f[0]:
            ans = 1
        else:
            ans = -1
    return ans

def special_move(vb, c1,c2):
    piece = vb[c1[0]][c1[1]]
    if piece[1] == 'p':
        if piece[0] == 'b' and c1[0] == 6 and c2[0] == 7:
            print('promotion')
            return 'promotion'
        elif piece[0] == 'w' and c1[0] == 1 and c2[0] == 0:
            print('promotion')
            return 'promotion'
    return None
    
def in_check_after_move(vb, c1, c2, color):
    board1 = vb.execute_move(c1,c2)
    out = in_check(board1, color)
    return out
def pre_score(board, color):
    return None
    """
    enemy_color = {'b':'w','w':'b'}[color]
    if not board.has_move(color):
        if in_check(board, color):
            return -np.inf
        else:
            return 0
    elif not board.has_move(enemy_color):
        if in_check(board, enemy_color):
            return np.inf
        else:
            return 0
    else:
        return None
    """
def in_checkmate(board, color):
    if in_check(board, color):
        mvs = board.get_next_boards(color)
        if len(mvs) == 0:
            #print(color + ' loses! Sucks to suck!')
            return True
    else:
        return False
def in_stalemate(board):
    mvs = board.get_next_boards('w')
    mvs2 = board.get_next_boards('b')
    #incheck = in_check(board, color)
    if len(mvs)+len(mvs2) == 0: #and incheck:
        #print('Stalemate')
        return True
    else:
        return False 
def in_check(board, color):
    king_coord = (-1,-1)
    enemy_moves = []
    enemy_color = {'b':'w','w':'b'}[color]
    for i in range(8):
        for j in range(8):
            occ = board[i][j]
            if occ != EMPTY:
                if occ == [color, 'k']:
                    king_coord = (i,j)
                if enemy_color == occ[0]:
                    enemy_moves.extend(moves_pre_check(board, (i,j), enemy_color))
    if king_coord == (-1,-1):
        raise ValueError(f"{color} king not found")
    if king_coord in enemy_moves:
        return True
    else:
        return False

def moves(board_array, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    dirty_moves = moves_pre_check(board_array, coords, color)
    out = []
    if dirty_moves is None:
        return None
    else:
        for mv in dirty_moves:
            if not in_check_after_move(board_array, coords, mv, color):
                out.append(mv)
    return out

def moves_pre_check(board, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    piece = board[coords[0]][coords[1]]
    out = []
    if piece == EMPTY:
        #print(coords)
        return None
    name = piece[1]
    if coords is None:
        print('No Moves')
        return None
    if piece[0] != color:
        return None
    elif name is 'n':# Knight
        for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1),
                    (-2,-1)]:
            out0 = np.array([x,y]) + coords
            if in_board_space(out0) and friendly_fire(board, coords, out0) != 1:
                out.append(tuple(out0))

    elif name is 'b': # Bishop
        for sign in [-1,1]:
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(board, coords, out0)
                        if fire == 0:
                            out.append(tuple(out0))
                        elif fire == -1:
                            out.append(tuple(out0))
                            break
                        elif fire == 1:
                            break

    elif name is 'r': # Rook
        for sign in [-1,1]:
            for xy in [np.array([1,0]), np.array([0,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(board, coords, out0)
                        if fire == 0:
                            out.append(tuple(out0))
                        elif fire == -1:
                            out.append(tuple(out0))
                            break
                        elif fire == 1:
                            break
                        
    elif name is 'q': # Queen
        for sign in [-1,1]:
            for xy in [np.array([1,0]), np.array([0,1]), np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(board, coords, out0)
                        if fire == 0:
                            out.append(tuple(out0))
                        elif fire == -1:
                            out.append(tuple(out0))
                            break
                        elif fire == 1:
                            break
    elif name is 'k':
        for sign in [-1,1]:
            for xy in [np.array([1,0]), np.array([0,1]), np.array([-1,1]), np.array([1,1])]:
                out0 = coords + sign*xy
                if in_board_space(out0):
                    if friendly_fire(board, coords, out0) in [0,-1]:
                        out.append(tuple(out0))

    elif name is 'p': # Pawn
        sign = {'b':1,'w':-1}[piece[0]]
        for move,fire in [[(sign,0), 0], [(sign,1),-1],[(sign,-1),-1]]:
            out0 = np.array(move) + coords
            if in_board_space(out0) and fire==friendly_fire(board, coords, out0):
                out.append(tuple(out0))
    else:
        raise ValueError

    return out




if __name__ == '__main__':
    pass

