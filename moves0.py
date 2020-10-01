#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 20:47:35 2020

@author: ted
"""
import numpy as np
from Virtual_board import VBoard

def in_board_space(xy, a=0,b=7):
        if a-1 < xy[0] < b+1 and a-1 < xy[1] < b+1:
            return True
        else:
            return False;
        
def friendly_fire(sq_dict, coord0, coord_f):
    occ_0 = sq_dict[tuple(coord0)].occupant
    occ_f = sq_dict[tuple(coord_f)].occupant
    if occ_f is not None:
        if occ_0[0] == occ_f[0]:
            ans = 1
        else:
            ans = -1
    else:
        ans = 0
    return ans

def in_check_after_move(sq_dict_before, c1, c2, color):
    king_coord = (0,0)
    enemy_moves = []
    enemy_color = {'b':'w','w':'b'}[color]
    board1 = VBoard(sq_dict_before).execute_move(c1,c2)
    '''
    out = in_check(board1, color)
    return out
    '''
    for sq in board1.sq_dict.values():
        occ = sq.occupant
        if occ is not None:
            if occ == color + 'k':
                king_coord = sq.index
            if enemy_color == occ[0]:
                enemy_moves.extend(moves_pre_check(board1.sq_dict, sq.index, enemy_color))
    if king_coord in enemy_moves:
        return True
    else:
        return False
    
def in_check(board, color):
    king_coord = (0,0)
    enemy_moves = []
    enemy_color = {'b':'w','w':'b'}[color]
    for sq in board.sq_dict.values():
        occ = sq.occupant
        if occ is not None:
            if occ == color + 'k':
                king_coord = sq.index
            if enemy_color == occ[0]:
                enemy_moves.extend(moves_pre_check(board.sq_dict, sq.index, enemy_color))
    if king_coord in enemy_moves:
        return True
    else:
        return False

def moves(sq_dict, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    dirty_moves = moves_pre_check(sq_dict, coords, color)
    out = []
    if dirty_moves is None:
        return None
    else:
        for mv in dirty_moves:
            if not in_check_after_move(sq_dict, coords, mv, color):
                out.append(mv)
    return out

def moves_pre_check(sq_dict, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    sq = sq_dict[tuple(coords)]
    out = []
    if sq.occupant is None:
        #print(coords)
        return None
    name = sq.occupant[1]
    if coords is None:
        print('No Moves')
        return None
    if sq.occupant[0] != color:
        return None
    elif name is 'n':# Knight
        for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1),
                    (-2,-1)]:
            out0 = np.array([x,y]) + coords
            if in_board_space(out0) and friendly_fire(sq_dict, coords, out0) != 1:
                out.append(tuple(out0))

    elif name is 'b': # Bishop
        for sign in [-1,1]:
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(sq_dict, coords, out0)
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
                        fire = friendly_fire(sq_dict, coords, out0)
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
                        fire = friendly_fire(sq_dict, coords, out0)
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
                    if friendly_fire(sq_dict, coords, out0) in [0,-1]:
                        out.append(tuple(out0))

    elif name is 'p': # Pawn
        sign = {'b':1,'w':-1}[sq.occupant[0]]
        for move,fire in [[(sign,0), 0], [(sign,1),-1],[(sign,-1),-1]]:
            out0 = np.array(move) + coords
            if in_board_space(out0) and fire==friendly_fire(sq_dict, coords, out0):
                out.append(tuple(out0))
    else:
        raise ValueError

    return out




if __name__ == '__main__':
    
    
    pass

