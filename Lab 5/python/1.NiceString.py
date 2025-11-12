class Solution:
    def longestNiceSubstring(self, s):
        if len(s)<2: return ''
        for i,c in enumerate(s):
            if c.swapcase() not in s:
                a=self.longestNiceSubstring(s[:i])
                b=self.longestNiceSubstring(s[i+1:])
                return a if len(a)>=len(b) else b
        return s