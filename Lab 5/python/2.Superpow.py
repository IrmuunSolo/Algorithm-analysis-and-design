from typing import List

class Solution:
    def superPow(self, a: int, b: List[int]) -> int:
        if not b: return 1
        return pow(a, b.pop(), 1337)* self.superPow(a,b) ** 10 % 1337