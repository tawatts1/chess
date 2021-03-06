import subprocess
import time




string1 = "br_bn_bb_00_bk_bb_bn_br=00_00_bp_bp_00_bp_bp_bp=bp_bp_00_00_bp_bq_00_00=00_00_00_00_00_00_00_00=00_00_wb_00_00_00_wq_00=00_00_wp_00_wp_00_00_wp=wp_wp_00_wp_00_wp_wp_00=wr_wn_wb_00_wk_00_wn_wr"
string2 = "br_00_bb_00_00_00_00_00=bp_bp_00_bp_bk_bp_bp_br=00_00_00_00_bp_bn_00_bp=00_bq_00_wp_00_00_00_00=00_00_wn_00_00_wp_00_00=wp_wn_00_wp_00_00_wp_00=00_00_wp_00_00_00_wb_wp=wr_00_00_wq_wk_00_00_wr"
string3 = "br_bn_bb_00_bk_bb_00_br=00_bp_00_bp_bp_bp_bp_bp=bp_00_bp_00_00_00_00_bn=00_00_00_00_00_00_bq_00=00_00_00_wp_00_00_00_00=00_00_00_wb_wp_00_wp_00=wp_wp_wp_wb_00_wp_00_wp=wr_wn_00_wq_wk_00_wn_wr"
string4 = "00_br_bb_00_bk_bb_bn_br=bp_00_bp_bp_00_bp_bp_bp=00_bp_00_00_bp_00_00_00=bn_00_00_00_00_00_bq_00=00_00_00_wp_wp_00_00_00=00_wp_wp_wb_00_00_wp_00=wp_00_00_00_00_wp_00_wp=wr_wn_wb_wq_wk_00_wn_wr"
string5 = "00_bn_00_bq_bk_bb_00_br=00_bp_bp_00_00_00_00_bp=00_00_00_bp_bp_bp_00_00=bp_00_00_wp_00_00_bp_00=00_00_00_00_00_00_00_00=00_00_wp_00_wq_00_wp_00=wp_wp_00_00_00_00_00_wp=wr_wn_wb_00_wk_00_wn_wr"
string6 = "00_00_00_00_br_bk_00_br=bn_00_bp_00_00_bp_bp_00=bp_00_00_bp_bb_00_bq_bp=00_bp_00_00_bp_bn_00_00=wp_wp_00_00_00_00_wp_00=00_00_00_00_00_wp_wk_00=wr_wq_wp_wp_wp_00_wb_wp=wb_wn_00_00_00_00_00_wr"
#br_bn_00_bq_bk_bb_bn_00=00_bb_00_00_bp_00_00_br=00_00_00_bp_00_00_00_00=00_00_bp_00_00_00_wb_wr=bp_00_00_00_wp_00_00_00=00_00_wq_wp_00_wb_00_00=wp_wp_00_00_00_00_00_00=wr_wn_00_00_wk_00_wn_00
string7 = "br_bn_00_00_bk_00_bn_00=00_bb_00_00_bp_00_wr_00=00_00_00_00_00_00_00_00=bq_00_bp_00_00_00_wb_wb=bp_00_bp_00_wp_00_00_00=00_00_wp_wp_00_00_00_00=wp_00_00_00_00_00_00_00=wr_00_00_00_wk_00_wn_00"
string8 = "br_bn_bb_00_bk_bb_bn_br=00_00_00_bp_00_00_00_00=bp_00_00_00_00_bp_00_00=bq_bp_wp_00_bp_wn_00_00=00_00_00_00_wp_00_00_00=wn_00_wp_wb_wb_00_00_00=wp_wp_00_00_00_00_wp_wp=wr_00_00_wq_wk_00_00_wr"


file = 'next_move'
'''
in the case that byte N = 5;
string1: 
0,0,1,0
0,1,2,2
0,2,1,1
0,5,3,2
1,2,2,2
2,1,3,1
2,5,3,4
2,5,2,6
2,5,2,7
72

string 2: 
describes a mid game scenario with both queens out and 
most pieces still alive
0,0,0,1
1,0,2,0
1,3,2,3
1,6,2,6
1,7,0,7
2,4,3,3
2,5,0,6
2,5,0,4
2,7,3,7
3,1,4,0
80

string3: 
early game scenario threatening queen
2,7,4,6
2,7,0,6
3,6,3,3
3,6,2,5
32

string4:
threaten queen directly
3,6,2,5
3,6,0,3
16
br_bn_00_bq_bk_bb_bn_00=00_bb_00_00_bp_00_00_br=00_00_00_bp_00_00_00_00=00_00_bp_00_00_00_wb_wr=bp_00_00_00_wp_00_00_00=00_00_wq_wp_00_wb_00_00=wp_wp_00_00_00_00_00_00=wr_wn_00_00_wk_00_wn_00
br_bn_00_00_bk_00_bn_00=00_bb_00_00_bp_00_wr_00=00_00_00_00_00_00_00_00=bq_00_bp_00_00_00_wb_wb=bp_00_bp_00_wp_00_00_00=00_00_wp_wp_00_00_00_00=wp_00_00_00_00_00_00_00=wr_00_00_00_wk_00_wn_00


'''
#print(string)
for string in [string1, string2, string3, string4, string5, string6, string7, string8]:
    t0 = time.time()
    move =  subprocess.run(
                        ['java',file, string,'b','5', 'list_all', 'knight', '0', 'pos'],
                        check=True, 
                        stdout=subprocess.PIPE).stdout.decode('ascii')
    print(move[:-1])
    #move = [int(x) for x in move.split(',')]
    print(len(move))
    print('time: ', time.time()-t0)
    print()


'''
on dec 19, after implementing a rudimentary prioritization approach, for N=5:
40
5.2
7.5
5.7
accurate to about 10% or so. 



dec 24, scoring with position, N=5:
12
17
23
10
'''
