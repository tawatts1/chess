#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Sep 12 14:53:51 2020

@author: ted
"""
import tkinter as tk
from tkinter import ttk
def none_callback():
    print('None callback')

class Square(tk.Button):
    def __init__(self, parent, img = None, color = 'grey', 
                 index = None, occupant = None):
        super().__init__(parent)
        
        self.parent = parent
        self.index = index
        self.occupant = occupant
        
        if img is None:
            photo = tk.PhotoImage()
        else:    
            photo = tk.PhotoImage(file = img)
        
        if occupant == None:
            self.config(image = photo, bg = color, 
                                height = 60, width = 60)#, command = none_callback)
        else:
            self.config(image = photo, bg = color, 
                                height = 60, width = 60)#, command = self.get_moves)
            
        self.image = photo
        self.grid(row=0,column=0)
        self.command = None
    
    def change_occupant(self, new_occupant):
        if new_occupant is None:
            self.occupant = None
        else:
            new_occupant.record_move(self.index)
            self.occupant = new_occupant
    def change_photo(self, fname):
        if fname is not None:
            photo = tk.PhotoImage(file = fname)
        else:
            photo = tk.PhotoImage()
        self.config(image = photo)
        self.image = photo
    def set_command(self,cmd):
        self.command = cmd
    
if __name__ == '__main__':
    
    root = tk.Tk()
    s1 = Square(root, img = 'images/bq.png')
    root.mainloop()
