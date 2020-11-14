import subprocess
import time



t0 = time.time()
string = "br_bn_bb_00_bk_bb_bn_br=00_00_bp_bp_00_bp_bp_bp=bp_bp_00_00_bp_bq_00_00=00_00_00_00_00_00_00_00=00_00_wb_00_00_00_wq_00=00_00_wp_00_wp_00_00_wp=wp_wp_00_wp_00_wp_wp_00=wr_wn_wb_00_wk_00_wn_wr"
string2 = "br_00_bb_00_00_00_00_00=bp_bp_00_bp_bk_bp_bp_br=00_00_00_00_bp_bn_00_bp=00_bq_00_wp_00_00_00_00=00_00_wn_00_00_wp_00_00=wp_wn_00_wp_00_00_wp_00=00_00_wp_00_00_00_wb_wp=wr_00_00_wq_wk_00_00_wr"
file = 'next_move'
'''
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
'''
#print(string)
move =  subprocess.run(
                    ['java',file, string2],
                    check=True, 
                    stdout=subprocess.PIPE).stdout.decode('ascii')
print(move)
#move = [int(x) for x in move.split(',')]
print('time: ', time.time()-t0)
