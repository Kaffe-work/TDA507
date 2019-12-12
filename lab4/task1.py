import os
import math
DIR_PATH = "./"
threshold = 4
threshold_min = 3.7

OFFSET = 100

class Atom:
    def __init__(self, x: float, y: float, z: float, index: int) -> None:
        self.x = x
        self.y = y
        self.z = z
        self.index = index

def read_data(file_name):
    DATA_TYPE = 0
    index = 1
    X = 6
    Y = 7
    Z = 8    
    parsed_lines = []
    with open(file_name, 'r') as file:
        line = file.readline()
        while line:
            #fuck you mr H"END"RICKSSON
            #line = line.replace("\t", " ")
            line = list(filter(None, line.split(" ")))
            if line[DATA_TYPE] == "ATOM" or line[DATA_TYPE] == "HETATM":
                atom = Atom(float(line[X]), float(line[Y]), float(line[Z]), int(line[index]))
                parsed_lines.append(atom)
            line = file.readline()
    return parsed_lines

def distance(a1, a2):
    """Calculates the euclidian distance between two atoms"""
    return math.sqrt((a1.x - a2.x)**2 + (a1.y - a2.y)**2 + (a1.z - a2.z)**2)

def brute_solution(first: list, second: list) -> None:
    overlaps = 0
    comps = 0
    for a in second:
        [c, o] = find_overlap(a, first)
        overlaps += o
        comps += c
    print("comparisons: {}, overlaps: {}".format(comps, overlaps))

def find_overlap(a: Atom, atoms: list) -> list:
    
    comps = 0
    
    for pos in atoms:
        comps +=1
       # print("hej2")
        if distance(a, pos) < threshold:
          #  print("hej3")
            print(a.index, pos.index)
            return [comps, 1]
    return [comps, 0]

def matrix_solution (first: list, second: list) -> None:
    overlaps = 0
    comps = 0
    mat = create_matrix(first)
  #  unit_test(first)
    print(len(first))
    print(len(second))
    for a in second:
        [c, o] = matrix_overlap(mat, a)
        overlaps += o
        comps += c
    print("comparisons: {}, overlaps: {}".format(comps, overlaps))

def create_matrix(atoms: list):
     print("hello")
     matrix = [[[[] for k in range(2 * OFFSET)] for j in range(2 * OFFSET)] for i in range(2 * OFFSET)]
     for a in atoms:
         matrix[int(a.x)+OFFSET][int(a.y)+OFFSET][int(a.z)+OFFSET].append(a)
     return matrix

def unit_test(ls: list) -> bool:
    l = 0
    mat = create_matrix(ls)
    for l in range(len(mat)):
        for ll in range(len(mat[l])):
            for lll in range(len(mat[l][ll])):
                l += len(mat[l][ll][lll])
    print(len(ls), l)

def matrix_overlap(m, a):
    comps = 0
    xs = calc_ster_overlap(a.x)
    ys = calc_ster_overlap(a.y)
    zs = calc_ster_overlap(a.z)
    for x in xs:
        for y in ys:
            for z in zs:
                ls = m[x+OFFSET][y+OFFSET][z+OFFSET]
                emptyspace_atom = Atom(x, y, z, 0)
                if distance(a, emptyspace_atom) < 5:
                    for at in ls:
                        comps +=1 #assume we are not checking atom against itself, could be tested if this is a probable case. 
                        if distance(a, at) < threshold:
                            return [comps, 1]

    return [comps, 0]




def calc_ster_overlap(i):
    return range(int(i-threshold)-1, int(i+threshold)+1)

def main():
    file_name = input("Give a path to pdb file:").upper()
    file_name = os.path.join(DIR_PATH, "{}.pdb".format(file_name))
    first = read_data(file_name)

    file_name_2 = input("Give a path to another pdb file:").upper()
    file_name_2 = os.path.join(DIR_PATH, "{}.pdb".format(file_name_2))
    second = read_data(file_name_2)
    print(len(first), len(second))
    #brute_solution(first, second)
    matrix_solution(first, second)

if __name__ == '__main__':
    main()