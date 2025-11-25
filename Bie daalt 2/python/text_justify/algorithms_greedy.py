from typing import List, Sequence
from .helpers import format_line


def greedy_justify(words: Sequence[str], max_width: int) -> List[str]:
    lines: List[str] = []
    i = 0
    n = len(words)
    while i < n:
        line_len = len(words[i])
        j = i + 1
        while j < n and line_len + 1 + len(words[j]) <= max_width:
            line_len += 1 + len(words[j])
            j += 1
        line_words = list(words[i:j])
        is_last = j >= n
        lines.append(format_line(line_words, max_width, is_last))
        i = j
    return lines
