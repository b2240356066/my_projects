import locale
import re
import sys
import string

locale.setlocale(locale.LC_ALL, "en_US")


input_file_path = sys.argv[1]
output_file_path = sys.argv[2]

with open(input_file_path, "r") as f:
    my_file = f.read()
    words = my_file.split()

#count the number of words
def count_words(words):
    number_of_words = len(words)
    word = "#Words"
    print("{:24}:".format(word) , number_of_words)
    return number_of_words

#count the number of sentence
def count_sentences(my_file):
    sentence_number = list(re.findall(r'[^.!?][.!?]:?\.{3}|[^.!?][.!?]*[.!?]', my_file))
    # ı used built in re library to find all the punctuations the sentences that are not inside the word such as (well-known) and ı wanted to count to find the number of sentences.
    number_of_sentences = len(sentence_number)
    sentence = "#Sentences"
    print("{:24}:".format(sentence) , number_of_sentences)
    return number_of_sentences

#finds all the characters
def find_number_of_characters(my_file):
    num_all_characters = len(my_file)
    character = "#Characters"
    print("{:24}:".format(character) , num_all_characters)
    return num_all_characters


#divides number of words to number of sentences
def avg_num_of_words_per_sentences(my_file):
    number_of_words = len(words)
    sentence_number = list(re.findall(r'[^.!?][.!?]:?\.{3}|[^.!?][.!?]*[.!?]', my_file))
    #.!? ile başlamayan,sonra bir çok karakter gelebilen(*),ve .!? ve ... ile biten kelimeleri sayıyor.
    number_of_sentences = len(sentence_number)
    words_sentence = "#Words/#Sentences"
    avg = f"{number_of_words / number_of_sentences:.2f}"
    print("{:24}:".format(words_sentence) ,avg)
    return avg

#deletes the punctuations which are at the beginning or at the end then joins them without white spaces so the length of it equals to number of char. without punc.
def strip_end_start_punc(words):
    stripped_words = [word.strip(string.punctuation) for word in words]
    strip_word = "".join(stripped_words)
    num_of_char = "#Characters (Just Words)"
    print("{:24}:".format(num_of_char),len(strip_word))
    return len(strip_word)

# find both the frequencies and length of the words together. created two lists and then zip it and turn it into a dictionary.
def length_of_words():
    number_of_words = len(words)
    stripped_words = [word.strip(string.punctuation) for word in words]
    strip_word = " ".join(stripped_words)
    # low_strip_word is lowercased, there is no punctuation and splitted list
    low_strip_word = strip_word.lower()
    list1 = low_strip_word.split(" ")
    # I used a set to not repeat the same values over again. then I had to turn into a list because I wanted to print the outcome according to alphabetical order.
    list1_uniq_words = sorted(list(set(list1)))
    list1_uniq_count = [list1.count(word)/number_of_words for word in list1_uniq_words]
    # find the longest word's length
    longest_word_length = max(len(word) for word in list1)
    #if it is equal then put it into longest_word_list
    longest_word_list = [word for word in list1 if len(word) == longest_word_length]
    longest_word_freq_list = [list1.count(word) for word in longest_word_list]
    # find the shortest word's length
    shortest_word_length = min(len(word) for word in list1)
    # if it is equal then put it into shortest_word_list
    shortest_word_list = [word for word in list1 if len(word) == shortest_word_length]
    sorted_shortest_word = sorted(list(set(shortest_word_list)))
    shortest_word_freq_list = [list1.count(word) for word in sorted_shortest_word]
    my_dict = dict(zip(list1_uniq_words,list1_uniq_count))


    if len(sorted_shortest_word) == 1:
        shortest_word_write = "The Shortest Word"
        print("{:24}:".format(shortest_word_write), "{:24}".format(shortest_word_list[0]),f"({shortest_word_freq_list[0]/number_of_words:.4f})")

    else:
        shortest_words_write = "The Shortest Words"
        sorted_shortest_word = sorted(list(set(shortest_word_list)))
        print("{:24}:".format(shortest_words_write))
        for i in range(len(sorted_shortest_word)):
            print("{:24}:".format(sorted_shortest_word[i]),f"{shortest_word_freq_list[i]/number_of_words:.4f}")

    if len(longest_word_list) == 1:
        longest_word_write = "The Longest Word"
        print("{:24}:".format(longest_word_write),"{:24}".format(longest_word_list[0]),f"({longest_word_freq_list[0]/number_of_words:.4f})")

    else:
        longest_words_write = "The Longest Words"
        sorted_longest_word = sorted(list(set(longest_word_list))) #is used a set to eliminate duplicates of words
        print("{:24}:".format(longest_words_write))
        for i in range(len(sorted_longest_word)):
            print("{:24}".format(sorted_longest_word[i]), f"({longest_word_freq_list[i] / number_of_words:.4f})")

# with the dictionary is sorted out the values to descending order
    freq_sorted = sorted(my_dict.items(), key=lambda item: (-item[1], item[0]))
    words_and_freq = "Words and Frequencies"
    print("{:24}:".format(words_and_freq))
    for word,freq in freq_sorted:
        print("{:24}:".format(word), f"{freq:.4f}")

def main():
    with open(output_file_path, "w") as f:
        # Redirect standard output to the file
        original_stdout = sys.stdout
        sys.stdout = f
        print(f"Statistics about", "{:7}:".format(sys.argv[1]))
        count_words(words)
        count_sentences(my_file)
        avg_num_of_words_per_sentences(my_file)
        find_number_of_characters(my_file)
        strip_end_start_punc(words)
        length_of_words()
    with open(output_file_path, "r") as f:
           lines = f.readlines()
    with open(output_file_path, "w") as f:
           for line in lines[:-1] :
               f.write(line)
           f.write(lines[-1].rstrip('\n'))



if __name__ == "__main__" :
   main()