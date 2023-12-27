# Description
Implement a DFSA to RegExp Translator. Given an DFSA description in the input.txt my program should output to console an error description (see validation errors) OR a regular expression that corresponds to the given DFSA. The regular expression should be built according to a slightly modified version of the Kleene’s algorithm (see Kleene's algorithm).
## Input file format:
- states=[s1,s2,...] 	// s1 , s2, ... ∈ latin letters, words and numbers
- alpha=[a1,a2,...]	// a1 , a2, ... ∈ latin letters, words, numbers and character '_’(underscore)
- initial=[s]	// s ∈ states
- accepting=[s1,s2,...]	// s1, s2 ∈ states
- trans=[s1>a>s2,...]	// s1,s2,...∈ states; a ∈ alpha

## Validation result
The errors may appear in the inputs, which should lead to error message according to the priority given below. Only 1 error message should be shown, if required. It should be assumed that for each line read from the inputs all the possible errors should be checked in the given priority, if applicable for the current line.

## Errors:
- E1: Input file is malformed
- E2: Initial state is not defined
- E3: Set of accepting states is empty
- E4: A state 's' is not in the set of states
- E5: A transition 'a' is not represented in the alphabet
- E6: Some states are disjoint
- E7: FSA is nondeterministic
