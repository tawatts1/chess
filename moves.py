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
            ans = True
    else:
        ans = False
    return ans

def moves(sq_dict, coords):#, friendlies, enemies):
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
            for xy in [np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all() and not friendly_fire(sq_dict, coords, out0):
                        out.append(out0)

        elif name is 'r': # Rook
            for xy in [np.array([1,0]), np.array([0,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all() and not friendly_fire(sq_dict, coords, out0):
                        out.append(out0)
        elif name is 'q': # Queen
            for xy in [np.array([1,0]), np.array([0,1]),
                       np.array([-1,1]), np.array([1,1])]:
                for multiplier in range(-8,9):
                    out0 = coords + multiplier*xy
                    if in_board_space(out0) and not (out0 == coords).all() and not friendly_fire(sq_dict, coords, out0):
                        out.append(out0)
        elif name is 'k':
            for x in range(-1,2):
                for y in range(-1,2):
                    out0 = coords + np.array([x,y])
                    if in_board_space(out0) and not (out0 == coords).all() and not friendly_fire(sq_dict, coords, out0):
                        out.append(out0)

        elif name is 'p': # Pawn
            sign = {'b':1,'w':-1}[sq.occupant[0]]
            for xy in [(sign,0), (sign,1),(sign,-1)]:
                out0 = np.array(xy) + coords
                if in_board_space(out0) and not friendly_fire(sq_dict, coords, out0):
                    out.append(out0)
        else:
            out = [coords]

        return out




if __name__ == '__main__':
    
    
    pass

