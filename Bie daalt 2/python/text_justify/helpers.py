from typing import Sequence


def line_length(words: Sequence[str], i: int, j: int) -> int:
    # length of words[i..j] with single spaces between
    return sum(len(w) for w in words[i : j + 1]) + (j - i)


def badness(max_width: int, used: int) -> int:
    # cube penalty of remaining spaces
    rem = max_width - used
    return rem * rem * rem


def format_line(words: Sequence[str], max_width: int, is_last: bool) -> str:
    if not words:
        return " " * max_width
    if len(words) == 1 or is_last:
        s = " ".join(words)
        if len(s) >= max_width:
            return s
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

