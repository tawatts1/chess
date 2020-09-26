#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 25 20:00:15 2020

@author: ted
"""
import numpy as np
from Square import Square

def index_to_piece(index):
    if index[0] == 1:
        out = 'bp'
    elif index[0] == 6:
        out = 'wp'
    elif index[0] == 0:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'b' + out0[index[1]]
    elif index[0] == 7:
        out0 = {0:'r', 1:'n', 2:'b', 3:'q', 4:'k', 5:'b', 6:'n', 7:'r'}
        out = 'w' + out0[index[1]]
    else:
        return None
    return out


def piece_to_fname(piece):
    if piece is None:
        return None
    elif len(piece) == 2:
        if piece[0] in 'bw':
            if piece[1] in 'kqbnrp':
                return 'images/' + piece + '.png'
    return None
def index_to_fname(index, piece_f = piece_to_fname):
    return piece_f(  index_to_piece(index)  )

def standard_game(root):
        board0 = []
        boardstr = []
        sq_dict = {}
        blist = []
        wlist = []
        for i in range(8):
            row = []
            rowstr = []
            for j in range(8):
                full_name = index_to_piece([i,j])
                fname = piece_to_fname(full_name)
                coords0 = np.array([i,j])
                if isinstance(full_name, str):
                    occupant0 = full_name
                    # add to white or black list:
                    {'b':blist,
                        'w':wlist}[full_name[0]].append(coords0)
                else:
                    occupant0 = None
                sq = Square(root,
                            img = fname,
                            index = coords0,
                            occupant = occupant0)
                #sq.config(command = sq.get_moves)
                sq_dict[tuple(coords0)] = sq
                #squares.append(sq)
                sq.grid(row=i, column=j)
                row.append(sq)
                rowstr.append(occupant0)
            board0.append(row)
            boardstr.append(rowstr)
        
        return board0, boardstr, sq_dict



if __name__ == '__main__':
    
    
    pass

