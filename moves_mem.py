#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 20:47:35 2020

@author: ted
"""
import numpy as np
from Virtual_board_mem import VBoard_m

def in_board_space(xy, a=0,b=7):
        if a-1 < xy[0] < b+1 and a-1 < xy[1] < b+1:
            return True
        else:
            return False;
        
def friendly_fire(rsq_dict, coord0, coord_f):
    occ_0 = rsq_dict[coord0[0]][coord0[1]]
    occ_f = rsq_dict[coord_f[0]][coord_f[1]]
    if occ_f is not None:
        if occ_0[0] == occ_f[0]:
            ans = 1
        else:
            ans = -1
    else:
        ans = 0
    return ans

def special_move(sq_dict, c1,c2):
    piece = sq_dict[c1].occupant
    if piece[1] == 'p':
        if piece[0] == 'b' and c1[0] == 6 and c2[0] == 7:
            print('promotion')
            return 'promotion'
        elif piece[0] == 'w' and c1[0] == 1 and c2[0] == 0:
            print('promotion')
            return 'promotion'
    return None

def in_check_after_move(rsq_dict_before, c1, c2, color):
    board1 = VBoard_m(None, rsq_dict_before).execute_move(c1,c2)
    out = in_check(board1, color)
    return out

def pre_score(board, color):
    #return None
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
    
def in_checkmate(board, color):
    mvs = board.get_next_boards(color)
    incheck = in_check(board, color)
    if len(mvs) == 0 and incheck:
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
    king_coord = (0,0)
    enemy_moves = []
    enemy_color = {'b':'w','w':'b'}[color]
    for i in range(8):
        for j in range(8):
            occ = board.rsq_dict[i][j]
            if occ is not None:
                if occ == color + 'k':
                    king_coord = (i,j)
                if enemy_color == occ[0]:
                    enemy_moves.extend(moves_pre_check(board.rsq_dict, (i,j), enemy_color))
    if king_coord in enemy_moves:
        return True
    else:
        return False

def moves_m(rsq_dict, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    dirty_moves = moves_pre_check(rsq_dict, coords, color)
    #return dirty_moves
    out = []

    for mv in dirty_moves:
        if not in_check_after_move(rsq_dict, coords, mv, color): 
            out.append(mv)
    return out

def moves_pre_check(rsq_dict, coords, color):#, friendlies, enemies):
    '''
    This has all chess moves except:
        No two step pawn opening, 
        no en passant, 
        and no bridging. 
    '''
    occupant = rsq_dict[coords[0]][coords[1]]
    out = []
    if occupant is None:
        #print(coords)
        return None
    name = occupant[1]
    if coords is None:
        print('No Moves')
        return None
    if occupant[0] != color:
        return None
    elif name is 'n':# Knight
        for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1),
                    (-2,-1)]:
            out0 = np.array([x,y]) + coords
            if in_board_space(out0) and friendly_fire(rsq_dict, coords, out0) != 1:
                out.append(tuple(out0))

    elif name is 'b': # Bishop
        for sign in [-1,1]:
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(rsq_dict, coords, out0)
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
                        fire = friendly_fire(rsq_dict, coords, out0)
                        '''
                        if tuple(out0) == (0,6):
                            print('in 0,6')
                            print(fire, coords, out0)
                        '''
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
                        fire = friendly_fire(rsq_dict, coords, out0)
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
                    if friendly_fire(rsq_dict, coords, out0) in [0,-1]:
                        out.append(tuple(out0))

    elif name is 'p': # Pawn
        sign = {'b':1,'w':-1}[occupant[0]]
        for move,fire in [[(sign,0), 0], [(sign,1),-1],[(sign,-1),-1]]:
            out0 = np.array(move) + coords
            if in_board_space(out0) and fire==friendly_fire(rsq_dict, coords, out0):
                out.append(tuple(out0))
    else:
        print('unexpected piece type')
        raise ValueError

    return out




if __name__ == '__main__':
    from game_setup import index_to_piece
    '''
    import os
    import psutil
    process = psutil.Process(os.getpid())
    print(process.memory_info().vms*10**-6)  # in bytes 
    '''
    sqd = {}
    for i in range(8):
        row = []
        for j in range(8):
            row.append(index_to_piece((i,j)))
        sqd[i] = row
    vbm = VBoard_m(None, sqd)
    print(vbm)
    ccb = vbm.get_next_boards('b')
    for c1,c2, board in ccb:
        print(c1, c2)
    print(len(ccb))
    #vbm.execute_move((7,6),(5,5))
    vbm.execute_move((0,6),(2,5))
    print(vbm)
    #print(process.memory_info().vms*10**-6)

