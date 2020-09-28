#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Sep 24 20:47:35 2020

@author: ted
"""
import numpy as np

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

def moves(sq_dict, coords):#, friendlies, enemies):
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
    elif name is 'n':# Knight
        for x,y in [(2,1),(2,-1), (1,2),(1,-2), (-1,2),(-1,-2), (-2,1),
                    (-2,-1)]:
            out0 = np.array([x,y]) + coords
            if in_board_space(out0) and not friendly_fire(sq_dict, coords, out0):
                out.append(out0)

    elif name is 'b': # Bishop
        for sign in [-1,1]:
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(sq_dict, coords, out0)
                        if fire == 0:
                            out.append(out0)
                        elif fire == -1:
                            out.append(out0)
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
                            out.append(out0)
                        elif fire == -1:
                            out.append(out0)
                            break
                        elif fire == 1:
                            break
                        
    elif name is 'q': # Queen
        for sign in [-1,1]:
            for xy in [np.array([1,0]), np.array([0,1]),
                       np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(1,9):
                    out0 = coords + sign*multiplier*xy
                    if in_board_space(out0):
                        fire = friendly_fire(sq_dict, coords, out0)
                        if fire == 0:
                            out.append(out0)
                        elif fire == -1:
                            out.append(out0)
                            break
                        elif fire == 1:
                            break
    elif name is 'k':
        for sign in [-1,1]:
            for xy in [np.array([1,0]), np.array([0,1]),
                       np.array([-1,1]), np.array([1,1])]:
                out0 = coords + sign*xy
                if in_board_space(out0):
                    if friendly_fire(sq_dict, coords, out0) in [0,-1]:
                        out.append(out0)

    elif name is 'p': # Pawn
        sign = {'b':1,'w':-1}[sq.occupant[0]]
        for move,fire in [[(sign,0), 0], [(sign,1),-1],[(sign,-1),-1]]:
            out0 = np.array(move) + coords
            if in_board_space(out0) and fire==friendly_fire(sq_dict, coords, out0):
                out.append(out0)
    else:
        raise ValueError

    return out




if __name__ == '__main__':
    
    
    pass

