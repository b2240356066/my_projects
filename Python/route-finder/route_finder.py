import sys

def field_variables(input_lines):
    grid = []
    for line in input_lines:
        # turn strings into integers for further comparisons
        for_int_changing = []
        for item in line.strip("\n").split(" "):
            for_int_changing.append(int(item))
        grid.append(for_int_changing)
    # using tuple assignment to use my costs
    (cost1, cost2, cost3) =  tuple(grid[0])
    # grids first line is costs so using[1:] to use my rows
    grid = grid[1:]
    rows = len(grid)
    columns = len(grid[0])
    return grid, rows, columns, cost1, cost2, cost3

def creating_field(my_input):
    with open(my_input) as file:
        #using readlines() command to make a list of lines that are given as txt
        input_lines = file.readlines()
        grid, rows, columns, cost1, cost2, cost3 = field_variables(input_lines)
        return grid, rows, columns, cost1, cost2, cost3

def showing_best_path(my_output,min_cost,grid):

    with open(my_output,"w") as file:
        if min_cost==float("inf"):
            #if min_cost is "inf" that means that no path changed best_cost and there is no path.
            file.write("There is no possible route.")
            file.close()
        elif min_cost:
            #printing best_path and best_cost
            file.write(f"Cost of route: {min_cost}"+ "\n")
            for row in grid:
                for cell in row:
                    file.write("  ".join(str(cell)))
                    file.write(" ")
                file.write("\n")
            file.close()



def finding_cost_of_a_path(path,cost1,cost2,cost3,rows,columns,grid):
        # this function is used to find current path's cost.
        #to find vertical or horizontal sinkholes
        direction_ver_hor = [(1, 0), (-1, 0), (0, -1), (0, 1)]
        #to find diagonal sinkholes
        direction_diag = [(1, 1), (1, -1), (-1, 1), (-1, -1)]

        current_cost = 0
        for i, j in path:
                cost3_found = False
                cost2_found = False
                cost1_found = False

                for di, dj in direction_ver_hor:
                    #if there is a sinkhole in vertical or horizontal cell turn the cost3_found to True
                    ni, nj = i + di, j + dj
                    if (0 <= ni < rows and 0 <= nj < columns) and grid[ni][nj] == 0:
                        cost3_found = True

                if not cost3_found:
                    #if cost3_found no need to check for other sinkholes.

                    for di, dj in direction_diag:
                        # if there is a sinkhole in diagonal cell turn the cost2_found to True
                        ni, nj = i + di, j + dj
                        if (0 <= ni < rows and 0 <= nj < columns) and grid[ni][nj] == 0:
                            cost2_found = True

                if not cost2_found and not cost3_found:
                    cost1_found = True

                #increase current cost with founded cost
                if cost3_found:
                    current_cost += cost3
                if cost2_found:
                    current_cost += cost2
                if cost1_found:
                    current_cost += cost1

        return current_cost



def find_the_path(grid,rows,columns, successful_paths,visited,path,cost1,cost2,cost3,best_cost, i , j):
    #if i or j is smaller than 0 or i or j is greater than rows and columns that means that they are out of the grid
    #if i,j is visited before we return to previous function
    if i<0 or i>=rows or j<0 or j>=columns or grid[i][j]==0 or (i,j) in visited:
        return successful_paths,best_cost

    #adding visited set current i,j to not visit this cell again
    visited.add((i,j))
    path.append((i,j))
    path_cost = finding_cost_of_a_path(path, cost1, cost2, cost3, rows, columns, grid) #to find the current path's cost
    # best_cost is the minimum cost of successful paths till now
    # if our current path's cost is greater than best_cost exit this path;if continued, cost of this path will increase so no need to check
    if path_cost > best_cost and len(successful_paths)>0:
        #removing last cell to continue finding paths with same origin in the recursion function.
        path.pop()
        visited.remove((i, j))
        return successful_paths,best_cost


    #this if statement tells we have a successful path without sinkholes.
    if j==columns-1:
        # using path.copy() to assure appending right path.
        successful_paths.append(path.copy())
        if path_cost < best_cost:
            #if best_cost is greater than our current path_cost change our best_cost to our current path_cost
            best_cost = path_cost
        path.pop()
        visited.remove((i, j))
        return successful_paths,best_cost



    else:
        #go right
        successful_paths,best_cost= find_the_path(grid,rows,columns,successful_paths,visited,path,cost1,cost2,cost3,best_cost,i,j+1)
        #go up
        successful_paths,best_cost=find_the_path(grid,rows,columns,successful_paths,visited,path,cost1,cost2,cost3,best_cost,i-1,j)
        #go down
        successful_paths,best_cost=find_the_path(grid, rows, columns, successful_paths, visited, path,cost1,cost2,cost3,best_cost, i+1 , j)
        #go left
        successful_paths,best_cost=find_the_path(grid, rows, columns, successful_paths, visited, path,cost1,cost2,cost3,best_cost, i, j-1)

    path.pop()
    visited.remove((i, j))

    return successful_paths,best_cost


def cost_of_path(successful_paths, rows, columns, grid, best_path, cost1, cost2, cost3):
    #this function is getting paths that are successfully finished
    # to check the surrounding of the cell firstly looking at vertical or horizontal sinkholes (cost3) if there is none checking diagonal sinkholes(cost2).
    # At last if we don't have anything we are using cost1.
    direction_ver_hor = [(1,0),(-1,0),(0,-1),(0,1)]
    direction_diag = [(1,1),(1,-1),(-1,1),(-1,-1)]
    #assign min_cost to infinity to compare to other paths
    min_cost= float("inf")

    for path in successful_paths:
        current_cost = 0
        for i,j in path:
            cost3_found = False
            cost2_found = False
            cost1_found = False

            for di, dj in direction_ver_hor:
                # if there is a sinkhole in vertical or horizontal cell turn the cost3_found to True
                ni,nj = i+di,j+dj
                if (0<= ni <rows and 0<= nj <columns) and grid[ni][nj]==0:
                    cost3_found = True

            if not cost3_found:
                #if cost3_found no need to check for other sinkholes.

                for di, dj in direction_diag:
                    # if there is a sinkhole in diagonal cell turn the cost2_found to True
                    ni,nj = i+di,j+dj
                    if (0<= ni <rows and 0<= nj <columns) and grid[ni][nj]==0:
                            cost2_found = True

            if not cost2_found and not cost3_found:
                cost1_found = True

            if cost3_found:
                current_cost += cost3
            if cost2_found:
                current_cost += cost2
            if cost1_found:
                current_cost += cost1
        #if our current cost is less than min_cost we change our min_cost to our found current_cost. save this path to mark with "X" our best_path.
        if current_cost < min_cost:
            min_cost = current_cost
            best_path = path
    #when we find the lowest cost path we need to mark our path in grid to X.
    for x,y in best_path:
        grid[x][y]= "X"
    return min_cost

def main():
    my_input = sys.argv[1]
    my_output = sys.argv[2]
    grid, rows, columns, cost1, cost2, cost3 = creating_field(my_input)

    #all the paths starting from left side and ending at right side without sinkholes.
    successful_paths = []
    #lowest cost path.
    best_path = []
    #the path that is current.
    path = []
    #used a set to not visit the same cell again.
    visited = set()
    #first best_cost will be infinity and will be compared by other paths costs.
    best_cost = float("inf")

    #Try every starting point on left edge
    for i in range(rows):
        #Only start if the cell at left is not a sinkhole.
        if grid[i][0]==1:
            successful_paths,best_cost = find_the_path(grid,rows,columns, successful_paths,visited,path, cost1,cost2,cost3,best_cost, i , 0)

    #find the cost of the best_path( path with the lowest cost)
    min_cost = cost_of_path(successful_paths,rows,columns,grid,best_path,cost1,cost2,cost3)
    #printing best_path on the grid with best_path's cost.
    showing_best_path(my_output, min_cost, grid)


if __name__ == '__main__':
    main()