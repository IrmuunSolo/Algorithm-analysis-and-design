from typing import List, Sequence


def _line_length(words: Sequence[str], i: int, j: int) -> int:
    # length of words[i..j] with single spaces between
    return sum(len(w) for w in words[i : j + 1]) + (j - i)


def _badness(max_width: int, used: int) -> int:
    # cube penalty of remaining spaces
    rem = max_width - used
    return rem * rem * rem


def dp_justify(words: Sequence[str], max_width: int) -> List[str]:
    n = len(words)
    inf = 10**18
    cost = [0] * (n + 1)
    next_break = [-1] * (n + 1)

    # DP from the end: cost[i] = minimal badness from i to end
    cost[n] = 0
    next_break[n] = -1
    for i in range(n - 1, -1, -1):
        best = inf
        best_j = i
        j = i
        while j < n:
            used = _line_length(words, i, j)
            if used > max_width:
                # Allow a single very long word to occupy its own line
                if j == i:
                    c = 0 if j == n - 1 else 0
                    total = c + cost[j + 1]
                    if total < best:
                        best = total
                        best_j = j
                break
            # last line gets zero cost to allow ragged-right last line
            c = 0 if j == n - 1 else _badness(max_width, used)
            # discourage single-word lines except when a single word is the only fit
            if j == i and j != n - 1 and (j + 1 < n) and (_line_length(words, i, j + 1) <= max_width):
                c += 10**6
            total = c + cost[j + 1]
            if total < best:
                best = total
                best_j = j
            j += 1
        cost[i] = best
        next_break[i] = best_j

    # Reconstruct breaks
    lines: List[List[str]] = []
    i = 0
    while i < n:
        j = next_break[i]
        if j < i:  # fallback (should not happen)
            j = i
        lines.append(list(words[i : j + 1]))
        i = j + 1

    # Format lines with even spacing (last line left-justified)
    out: List[str] = []
    for idx, line_words in enumerate(lines):
        is_last = idx == len(lines) - 1
        if not line_words:
            out.append(" " * max_width)
            continue
        if len(line_words) == 1 or is_last:
            s = " ".join(line_words)
            out.append(s + " " * (max_width - len(s)))
        else:
            total_chars = sum(len(w) for w in line_words)
            spaces_needed = max_width - total_chars
            slots = len(line_words) - 1
            base = spaces_needed // slots
            extra = spaces_needed % slots
            parts = []
            for k, w in enumerate(line_words[:-1]):
                gap = base + (1 if k < extra else 0)
                parts.append(w + (" " * gap))
            parts.append(line_words[-1])
            out.append("".join(parts))
    return out
