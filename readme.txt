Anna Reis uni aer2221
Data Structures Capstone Project

What data structure do you expect to have the best (fastest) performance? Which one do you expect to be the slowest? Do the results of timing your program’s execution match your expectations? If so, briefly explain the correlation. If not, what run times deviated and briefly explain why you think this is the case.

I would expect HashMap to have the best performance because, in its best and average case, it carries constant theta(1) lookup time. If one chooses a good hash function, elements will be distributed relatively evenly throughout the array. This ensures that you do not have to traverse down a long chain at a given index to find an element; instead, one may simply get the element directly from the array itself. AVL and BST, on the other hand, do not allow for constant lookup in our implementations. 

I would expect BST to be the slowest because its worst case lookup time is theta(n). While BST's average lookup time of theta(lgn) is the same as AVL, this worst case is unique to BST. While this worst case would likely not occur frequently (only when values inserted after the root are all smaller or larger than the root itself), it is still possible, meaning BST will at large scales perform worse than both HashMap and AVL. 

For my programs, the HashMap is the fastest, AVL is in the middle, and BST is the slowest. This meets my expectations give what we learned about time complexity this year. HashMap's best and average constant lookup gives it a slight edge over the other two data structures which can almost never guarantee a constant time. AVL is a balanced BST, so it prevents the worst case linear lookup time of BST and thus is quicker than BST. Finally, BST comes last because of its lack of balancing, giving it the same average lookup as AVL but a significantly slower worst-case lookup. 





