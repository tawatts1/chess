#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:53:51 2020

@author: ted
"""
import tkinter as tk
from tkinter import ttk


class VSquare():
    def __init__(self, index = None, occupant = None):
        
        self.index = index
        self.occupant = occupant
    
    def change_occupant(self, new_occupant):
        self.occupant = new_occupant
    
    
if __name__ == '__main__':
    pass
