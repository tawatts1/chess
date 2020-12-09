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
    if piece is ['0','0']:
        return None
    elif piece[0] in 'bw':
            if piece[1] in 'kqbnrp':
                return 'images/' + piece[0] + piece[1] + '.png'
def index_to_fname(index, piece_f = piece_to_fname):
    return piece_f(  index_to_piece(index)  )



if __name__ == '__main__':
    
    
    pass

