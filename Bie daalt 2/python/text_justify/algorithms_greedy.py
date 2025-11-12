from typing import List, Sequence


def justify_line(words: Sequence[str], max_width: int, is_last: bool) -> str:
    if not words:
        return " " * max_width
    if len(words) == 1 or is_last:
        s = " ".join(words)
        return s + " " * (max_width - len(s))

    total_chars = sum(len(w) for w in words)
    spaces_needed = max_width - total_chars
    slots = len(words) - 1
    base = spaces_needed // slots
    extra = spaces_needed % slots

    parts = []
    for i, w in enumerate(words[:-1]):
        gap = base + (1 if i < extra else 0)
        parts.append(w + (" " * gap))
    parts.append(words[-1])
    return "".join(parts)


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
        lines.append(justify_line(line_words, max_width, is_last))
        i = j
    return lines
