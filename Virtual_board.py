#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 13 13:15:21 2020

@author: ted
"""
EMPTY = ['0','0']
BQ = ['b','q']
WQ = ['w','q']
BP = ['b','p']
WP = ['w','p']
BK = ['b','k']
WK = ['w','k']
BR = ['b','r']
WR = ['w','r']
BN = ['b','n']
WN = ['w','n']
BB = ['b','b']
WB = ['w','b']

import numpy as np
from move_calc import moves


class VBoard(list):
    def __init__(self, empty = False):
        if not empty:
            self = super(VBoard, self).__init__(gen_standard_board_array())
        else:
            self = super(VBoard, self).__init__([[EMPTY for j in range(8)] for i in range(8)])
    def moves(self, coord, color):
        return moves(self, coord, color)
    def get_next_boards(self, color):
        out = []
        for i in range(8):
            for j in range(8):
                occ = self[i][j]
                if occ != EMPTY and occ[0] == color:
                    mvs = self.moves((i,j), color)
                    for move in mvs:
                        board_1 = self.execute_move((i,j), move)
                        out.append([(i,j),move,board_1])
        return out
    def execute_move(self, c1, c2):
        out = VBoard(empty=True)
        # make careful duplicate, only copying the two squares
        for i in range(8):
            if i == c1[0] or i==c2[0]:
                for j in range(8):
                    if (i == c1[0] and j==c1[1]) or (i==c2[0] and j==c2[1]):
                        out[i][j] = self[i][j].copy()
                    else:
                        out[i][j] = self[i][j]
            else:
                out[i] = self[i]
        # make switch
        out[c2[0]][c2[1]] = self[c1[0]][c1[1]]
        out[c1[0]][c1[1]] = EMPTY
        return out
    def __str__(self):
        out = '-'
        for i in range(8):
            out += '------+'
        out+='\n'
        for i in range(8):
            s = ''
            for j in range(8):
                occ = self[i][j]
                if occ == EMPTY:
                    occ='  '
                    s+= '|  '+occ + '  '
                else:
                    s+= '|  '+occ[0] + occ[1] + '  '
            out +=s + '|\n-'
            for i in range(8):
                out += '------+'
            out+='\n'
        return out
def index_to_piece(index):
    if index[0] == 1:
        out = BP
    elif index[0] == 6:
        out = WP
    elif index[0] == 0:
        out0 = {0:BR, 1:BN, 2:BB,3:BQ, 
                4:BK, 5:BB, 6:BN, 7:BR}
        out = out0[index[1]]
    elif index[0] == 7:
        out0 = {0:WR, 1:WN, 2:WB,3:WQ, 
                4:WK, 5:WB, 6:WN, 7:WR}
        out = out0[index[1]]
    else:
        out = EMPTY
    return out         
def gen_standard_board_array():
    board = []
    for i in range(8):
        row = []
        for j in range(8):
            sq = index_to_piece((i,j))
            row.append(sq)
        board.append(row)
    return board 
    
    
if __name__ == '__main__':

    b1 = VBoard()
    print(b1)
    b1 = b1.execute_move((1,0),(2,0))
    print(b1)
    print(b1.moves((1,1),'b'))