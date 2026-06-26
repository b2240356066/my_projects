import sys

input_file = sys.argv[1]


def create_table(database,table_name, columns):
# creating columns and rows for a dictionary named after the given table name.
            database[table_name] = {
                "name" : table_name ,
                "columns" : columns,
                "rows" : []
            }
            print_operation("CREATE", table_name, f"Table '{table_name}' created with columns: {columns}",database )

def insert_row(database,table_name,rows):
    #inserting the given row to the dictionary under table_name. used a tuple to make it like ('3', 'Ted Wilson', '21', 'CS') to use ()
    will_print = ()
    for i in range(len(rows)):
        will_print = will_print + (rows[i],)
    try:
        database[table_name]["rows"].append(rows)
        print_operation("INSERT", table_name, f"Inserted into {table_name}: {will_print}", database, database[table_name]["columns"],database[table_name]["rows"])

    except KeyError:
        print_operation("INSERT", table_name, f"Table {table_name} not found\nInserted into '{table_name}': {will_print}",database)


def select(database,table_name,condition,required_columns):
#spliting the required columns by "," so we can continue by looking the index of the required columns in the table_names columns indexes. And if they match we continue by appending them to the wanted output.
    selected_columns = required_columns.strip().split(",")
    try:
        table = database[table_name]
        selected_rows = []
        for row in table["rows"]:
            #chech if condition is present.
            match = True
            for key,value in condition.items():
                if row[table["columns"].index(key)] != value:
                    match = False
                    break

            if  match:
                # if our condition is * , it implements that all the columns will be selected
                wanted = ()
                if "*" in selected_columns:
                    for i in range(len(row)):
                        wanted = wanted + (row[i],)
                    selected_rows.append(wanted)
                else:
                # else we take the index of the wanted column and look it in the tables columns
                    for col in selected_columns:
                        selected_row= row[table["columns"].index(col)]
                        wanted = wanted + (selected_row,)
                    selected_rows.append(wanted)

        print_operation("SELECT", table_name, f"Condition: {condition}\nSelect result from '{table_name}': {selected_rows}",database )
    except KeyError:
        not_chosen = None
        print_operation("SELECT", table_name,f"Table {table_name} not found\nCondition: {condition}\nSelect result from '{table_name}': {not_chosen}",database)
    except ValueError:
        not_chosen = None
        for i in selected_columns:
            # if the selected columns are not in the table_name columns in the dictionary we raise an error
            if i not in table["columns"]:
                not_founded_column = i
                print_operation("SELECT", table_name,f"Column {not_founded_column} does not exist\nCondition: {condition}\nSelect result from '{table_name}': {not_chosen}",database )
        for key,value in condition.items():
            #if our condition doesn't have the proper key, value we raise an error
            if key not in table["columns"]:
                not_founded_condition = key
                print_operation("SELECT", table_name,
                                f"Column {not_founded_condition} does not exist\nCondition: {condition}\nSelect result from '{table_name}': {not_chosen}",database)


def update(database,table_name, updates, where):
    rows_updated = 0
    try:
        #we find the index of the updates in table column if  they match we change it to wanted condition
        table = database[table_name]
        for row in table["rows"]:
            #chech if condition is present.
            match = True
            for key,value in where.items():
                if row[table["columns"].index(key)] != value:
                    match = False
                    break

            if  match:
                for key, value in updates.items():
                    row[table["columns"].index(key)] = value
                    rows_updated +=1
        print_operation("UPDATE", table_name, f"Updated '{table_name}' with {updates} where {where}\n{rows_updated} rows updated.",database, database[table_name]["columns"], database[table_name]["rows"])
    except KeyError:
        print_operation("UPDATE", table_name,f"Updated '{table_name}' with {updates} where {where}\nTable {table_name} not found\n{rows_updated} rows updated.",database)
    except ValueError:
        for key, value in updates.items():
            if key not in table["columns"]:
                # if the selected columns are not in the table_name columns in the dictionary we raise an error
                not_founded_updates = key
                print_operation("UPDATE", table_name,
                                f"Updated '{table_name}' with {updates} where {where}\nColumn {not_founded_updates} does not exist\n{rows_updated} rows updated.",database,
                                database[table_name]["columns"], database[table_name]["rows"])

        for key, value in where.items():
            # if our condition doesn't have the proper key, value we raise an error
            if key not in table["columns"]:
                not_founded_updates = key
                print_operation("UPDATE", table_name,
                                f"Updated '{table_name}' with {updates} where {where}\nColumn {not_founded_updates} does not exist\n{rows_updated} rows updated.",database,database[table_name]["columns"], database[table_name]["rows"])

def delete(database,table_name, where):
    rows_deleted = 0
    try:
        # if our condition is not given we delete all the rows. used clear function to erase them in a single step
        table = database[table_name]
        initial_row_count = len(table["rows"])
        new_rows = []
        for row in table["rows"]:
            if where == "None":
                rows_deleted = len(table["rows"])
                table["rows"].clear()
                break

            else:
                match = True
                #then we append the rows which are not the wanted condition (technically, erase the wanted condition)
                for key, value in where.items():
                    if row[table["columns"].index(key)] != value:
                        match = False
                        break
                if not match:
                    new_rows.append(row)
                table["rows"] = new_rows
                rows_deleted = initial_row_count - len(table["rows"])



        print_operation("DELETE", table_name, f"Deleted from '{table_name}' where {where}\n{rows_deleted} rows deleted.", database,database[table_name]["columns"], database[table_name]["rows"])

    except KeyError:
        print_operation("DELETE", table_name, f"Deleted from '{table_name}' where {where}\nTable {table_name} not found\n{rows_deleted} rows deleted.",database)
    except ValueError:
        for key, value in where.items():
            if key not in table["columns"]:
                # if our condition doesn't have the proper key, value we raise an error
                not_founded_condition = key
                print_operation("DELETE", table_name,
                            f"Deleted from '{table_name}' where {where}\nColumn {not_founded_condition} does not exist\n{rows_deleted} rows deleted.",database, database[table_name]["columns"], database[table_name]["rows"])


def count(database,table_name, where):
    rows_count = 0
    try:
        table = database[table_name]
        relevant_rows = []
        for row in table["rows"]:
            if where == "*":
                #if our condition is * count all the rows
                rows_count = len(table["rows"])
                break

            else:
                match = True
                for key, value in where.items():
                    # else count the rows which satisfy the given condition
                    if row[table["columns"].index(key)] != value:
                        match = False
                        break
                if match:
                        relevant_rows.append(row)
        if rows_count == 0:
            rows_count = len(relevant_rows)
        print_operation("COUNT", table_name,f"Count: {rows_count}\nTotal number of entries in {table_name} is {rows_count}",database)
    except KeyError:
        print_operation("COUNT", table_name,f"Table {table_name} not found\nTotal number of entries in '{table_name}' is {rows_count}",database)
    except ValueError:
        for key, value in where.items():
            # if our condition doesn't have the proper key, value we raise an error
            if key not in table["columns"]:
                not_founded_condition = key
                print_operation("COUNT", table_name,f"Column {not_founded_condition} does not exist\nTotal number of entries in '{table_name}' is {rows_count}",database)


def join_tables(table_1, table_2, joined_column,first_table,second_table,database):
    try:
        #take both of the tables from our database dictionary and find the index of the wanted column which will be used to join
        table1_wanted_column_idx = table_1["columns"].index(joined_column)
        table2_wanted_column_idx = table_2["columns"].index(joined_column)
        #we used concatenation to right all the rows and columns
        joined_columns = table_1["columns"] + table_2["columns"]
        joined_rows = []

        for table_1_rows in table_1["rows"]:
            for table_2_rows in table_2["rows"]:
                if table_1_rows[table1_wanted_column_idx] == table_2_rows[table2_wanted_column_idx]:
                    joined_rows.append(table_1_rows + table_2_rows)

        number_of_rows = f"{len(joined_rows)} rows"
        print_operation("JOIN", "Joined Table" , f"Join tables {first_table} and {second_table}\nJoin result ({number_of_rows}):",database, joined_columns, joined_rows)

    except ValueError:
        #if the wanted column is not in either table_1 or table_2 raise an error
        if joined_column not in table_1["columns"] or joined_column not in table_2["columns"]:
            print("###################### JOIN #########################")
            print(f"Join tables {first_table} and {second_table}\nColumn {joined_column} does not exist")
            print("#######################################################\n")


def print_table(columns, rows, table_name,database):
    #defined a function to make the table structure with appropriate length of -
    if table_name in database or table_name == "Joined Table":
        col_widths = [max(len(str(item)) for item in col) for col in zip(columns, *rows)] # using len function to find the longest value so that our table is suitable for this value
        header = f"+{'--+'.join('-' * width for width in col_widths)}--+"
        column_names = f"| {' | '.join(str(col).ljust(width) for col, width in zip(columns, col_widths))} |"
        print(f"\nTable: {table_name}")
        print(header)
        print(column_names)
        print(header)
        for row in rows:
            row_data = f"| {' | '.join(str(item).ljust(width) for item, width in zip(row, col_widths))} |"
            print(row_data)
        print(header)

def print_operation(operation, table_name, message ,database, columns=None , rows=None):
    #defined a function to make the proper output
    print(f"###################### {operation} #########################")
    print(message)
    if operation == "INSERT"  or operation == "UPDATE" or operation == "DELETE" or operation == "JOIN":
        print_table(columns, rows, table_name,database)
    print("#######################################################\n")



def parse_where_clause(where_clause):
    #defined a function to split the condition,updates part to a dictionary to use key,value in other functions
    conditions_dict = {}
    where_clause = where_clause.strip("{}")  # Remove the curly braces
    pairs = where_clause.split(", ")  # Split by comma and space
    for pair in pairs:
        key, value = pair.split(": ")
        key = key.strip("'\"") # Remove quotes from key
        value = value.strip("'\"")  # Remove quotes from value
        conditions_dict[key] = value
    return conditions_dict

def parse_command(database,p_command):
    #splits our command line to use proper variables in different functions
        parts =p_command.split(maxsplit=2)
        operation = parts[0]
        table_name = parts[1] if len(parts) > 1 else None
        if operation == "CREATE_TABLE":
            columns = parts[2].split(',')
            create_table(database, table_name, columns)

        elif operation == "INSERT":
            rows = parts[2].split(',')
            insert_row(database,table_name, rows)

        elif operation == "SELECT":
            required_columns, where_clause  = parts[2].split('WHERE ')
            condition = parse_where_clause(where_clause)
            select(database,table_name,condition,required_columns)

        elif operation == "UPDATE":
            update_clause, where_clause = parts[2].split('WHERE ')
            where = parse_where_clause(where_clause.strip())
            updates = parse_where_clause(update_clause.strip())
            update(database,table_name, updates, where)

        elif operation == "DELETE":
            if len(parts)>2:
                empty_clause, where_clause = parts[2].split('WHERE ')
                where = parse_where_clause(where_clause)
            else:
                where = "None"
            delete(database,table_name, where)

        elif operation == "COUNT":

            empty_clause, where_clause = parts[2].split('WHERE ')
            if where_clause == "*":
                where = where_clause
            else:
                where = parse_where_clause(where_clause)
            count(database,table_name, where)

        elif operation == "JOIN":
            table_1, table_2 = parts[1].split(",")
            first_table = table_1
            second_table = table_2
            try:
                empty_clause, joined_column = parts[2].split('ON ')
                join_tables(database[table_1], database[table_2],joined_column,first_table,second_table,database)
            except KeyError:
                if first_table not in database:
                    print(f"###################### {operation} #########################")
                    print(f"Join tables {first_table} and {second_table}\nTable {first_table} does not exist")
                    print("#######################################################\n")
                if second_table not in database:
                    print(f"###################### {operation} #########################")
                    print(f"Join tables {first_table} and {second_table}\nTable {second_table} does not exist")
                    print("#######################################################\n")



def main():
    database = dict()
    try:
        with open(input_file, 'r') as file:
            commands = file.readlines()

            for command in commands:
                command_to_be_executed=command.strip()
                if command_to_be_executed :
                    parse_command(database,command_to_be_executed)

    except FileNotFoundError:
        print(f"File not found: {input_file}")


if __name__ == "__main__":

    main()